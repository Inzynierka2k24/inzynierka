package com.inzynierka2k24.apiserver.dao;

import static org.junit.jupiter.api.Assertions.*;

import com.inzynierka2k24.ExternalService;
import com.inzynierka2k24.apiserver.model.ExternalOffer;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.annotation.DirtiesContext;

@JdbcTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ExternalOfferDaoTest {

  @Autowired private JdbcTemplate template;

  private ExternalOfferDao offerDao;

  @BeforeEach
  void setUp(@Autowired DataSource dataSource) throws SQLException {
    try (Connection connection = dataSource.getConnection()) {
      ScriptUtils.executeSqlScript(connection, new ClassPathResource("db-schema.sql"));
      ScriptUtils.executeSqlScript(connection, new ClassPathResource("offers-test-data.sql"));
    }

    offerDao = new ExternalOfferDao(template);
  }

  @Test
  void shouldRetrieveAllOffersForApartmentId() {
    // Given
    long apartmentId = 1L;
    int expectedSize = 1;
    var expectedOffers =
        List.of(new ExternalOffer(Optional.of(1L), ExternalService.AIRBNB, "Apartment"));

    // When
    List<ExternalOffer> offers = offerDao.getAll(apartmentId);

    // Then
    assertEquals(expectedSize, offers.size());
    assertEquals(expectedOffers, offers);
  }

  @Test
  void shouldRetrieveOfferByIdAndApartmentId() {
    // Given
    long apartmentId = 1;
    long offerId = 1;
    var expectedOffer = new ExternalOffer(Optional.of(1L), ExternalService.AIRBNB, "Apartment");

    // When
    Optional<ExternalOffer> actualOffer = offerDao.getById(apartmentId, offerId);

    // Then
    assertTrue(actualOffer.isPresent());
    assertEquals(expectedOffer, actualOffer.get());
  }

  @Test
  void shouldAddNewOfferForApartmentId() {
    // Given
    long apartmentId = 1;
    long offerId = 2;
    var offer = new ExternalOffer(Optional.of(2L), ExternalService.AIRBNB, "Apartment");

    // When
    offerDao.add(apartmentId, offer);

    // Then
    Optional<ExternalOffer> retrievedOffer = offerDao.getById(apartmentId, offerId);
    assertTrue(retrievedOffer.isPresent());
    assertEquals(offer, retrievedOffer.get());
  }

  @Test
  void shouldDeleteOfferByIdAndApartmentId() {
    // Given
    long apartmentId = 1;
    long offerId = 1;

    // When
    offerDao.deleteById(apartmentId, offerId);

    // Then
    Optional<ExternalOffer> deletedoffer = offerDao.getById(apartmentId, offerId);
    assertFalse(deletedoffer.isPresent());
  }

  @Test
  void shouldUpdateExistingOffer() {
    // Given
    var updatedOffer = new ExternalOffer(Optional.of(1L), ExternalService.AIRBNB, "Apartment");

    // When
    offerDao.update(updatedOffer);

    // Then
    Optional<ExternalOffer> retrievedOffer = offerDao.getById(1L, 1L);
    assertTrue(retrievedOffer.isPresent());
    assertEquals(updatedOffer, retrievedOffer.get());
  }

  @Test
  void shouldRetrieveOfferByApartmentIdAndServiceType() {
    // Given
    long apartmentId = 1;
    ExternalService serviceType = ExternalService.AIRBNB;
    var expectedOffer = new ExternalOffer(Optional.of(1L), ExternalService.AIRBNB, "Apartment");

    // When
    Optional<ExternalOffer> retrievedOffer = offerDao.getByServiceType(apartmentId, serviceType);

    // Then
    assertTrue(retrievedOffer.isPresent());
    assertEquals(expectedOffer, retrievedOffer.get());
  }

  @Test
  void shouldHandleInvalidOfferId() {
    // Given
    long apartmentId = 1;
    long offerId = -1;

    // When
    Optional<ExternalOffer> offer = offerDao.getById(apartmentId, offerId);

    // Then
    assertTrue(offer.isEmpty());
  }

  @Test
  void shouldHandleInvalidOfferIdWhenUpdatingOffer() {
    // Given
    long apartmentId = 1;
    long offerId = -1;
    Instant startDate = Instant.parse("2023-01-01T00:00:00Z");
    Instant endDate = Instant.parse("2023-01-07T00:00:00Z");
    ExternalOffer invalidOffer =
        new ExternalOffer(Optional.of(offerId), ExternalService.AIRBNB, "");

    // When
    offerDao.update(invalidOffer);

    // Then
    Optional<ExternalOffer> offer = offerDao.getById(apartmentId, offerId);
    assertFalse(offer.isPresent());
    assertDoesNotThrow(() -> DataAccessException.class);
  }

  @Test
  void shouldHandleInvalidApartmentIdWhenDeletingOffer() {
    // Given
    long apartmentId = -1;
    long offerId = 1;

    // When
    offerDao.deleteById(apartmentId, offerId);

    // Then
    assertDoesNotThrow(() -> DataAccessException.class);
  }

  @Test
  void shouldHandleInvalidOfferIdWhenDeletingOffer() {
    // Given
    long apartmentId = 1;
    long offerId = -1;

    // When
    offerDao.deleteById(apartmentId, offerId);

    // Then
    assertDoesNotThrow(() -> DataAccessException.class);
  }
}
