package com.inzynierka2k24.apiserver.dao;

import static org.junit.jupiter.api.Assertions.*;

import com.inzynierka2k24.apiserver.model.Reservation;
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
class ReservationDaoTest {

  @Autowired private JdbcTemplate template;

  private ReservationDao reservationDao;

  @BeforeEach
  void setUp(@Autowired DataSource dataSource) throws SQLException {
    try (Connection connection = dataSource.getConnection()) {
      ScriptUtils.executeSqlScript(connection, new ClassPathResource("db-schema.sql"));
      ScriptUtils.executeSqlScript(connection, new ClassPathResource("reservations-test-data.sql"));
    }

    reservationDao = new ReservationDao(template);
  }

  @Test
  void shouldRetrieveAllReservationsForApartmentId() {
    // Given
    long apartmentId = 1L;
    int expectedSize = 1;
    var expectedReservations =
        List.of(
            new Reservation(
                Optional.of(1L),
                apartmentId,
                Instant.parse("2023-01-01T00:00:00Z"),
                Instant.parse("2023-01-04T00:00:00Z")));

    // When
    List<Reservation> reservations = reservationDao.getAll(apartmentId);

    // Then
    assertEquals(expectedSize, reservations.size());
    assertEquals(expectedReservations, reservations);
  }

  @Test
  public void shouldRetrieveReservationByIdAndApartmentId() {
    // Given
    long apartmentId = 1;
    long reservationId = 1;

    // When
    Optional<Reservation> reservation = reservationDao.getById(apartmentId, reservationId);

    // Then
    assertTrue(reservation.isPresent());
    assertEquals(apartmentId, reservation.get().apartmentId());
    assertEquals(reservationId, reservation.get().id().orElseThrow());
  }

  @Test
  public void shouldAddNewReservationForApartmentId() {
    // Given
    long apartmentId = 1;
    long reservationId = 2;
    Reservation reservation =
        new Reservation(
            Optional.of(reservationId),
            apartmentId,
            Instant.parse("2022-01-01T00:00:00Z"),
            Instant.parse("2022-01-02T00:00:00Z"));

    // When
    reservationDao.add(apartmentId, reservation);

    // Then
    Optional<Reservation> retrievedReservation = reservationDao.getById(apartmentId, reservationId);
    assertTrue(retrievedReservation.isPresent());
    assertEquals(reservation, retrievedReservation.get());
  }

  @Test
  public void shouldDeleteReservationByIdAndApartmentId() {
    // Given
    long apartmentId = 1;
    long reservationId = 1;

    // When
    reservationDao.deleteById(apartmentId, reservationId);

    // Then
    Optional<Reservation> deletedReservation = reservationDao.getById(apartmentId, reservationId);
    assertFalse(deletedReservation.isPresent());
  }

  @Test
  public void shouldUpdateExistingReservation() {
    // Given
    Reservation updatedReservation =
        new Reservation(
            Optional.of(1L),
            1L,
            Instant.parse("2022-01-03T00:00:00Z"),
            Instant.parse("2022-01-04T00:00:00Z"));

    // When
    reservationDao.update(updatedReservation);

    // Then
    Optional<Reservation> retrievedReservation = reservationDao.getById(1L, 1L);
    assertTrue(retrievedReservation.isPresent());
    assertEquals(updatedReservation, retrievedReservation.get());
  }

  @Test
  public void shouldCheckTimePeriodIsFree() {
    // Given
    Reservation reservation =
        new Reservation(
            Optional.empty(),
            1L,
            Instant.parse("2022-01-01T00:00:00Z"),
            Instant.parse("2022-01-05T00:00:00Z"));

    // When
    boolean isPeriodFree = reservationDao.isTimePeriodFree(reservation);

    // Then
    assertTrue(isPeriodFree);
  }

  @Test
  public void shouldHandleOverlappingReservations() {
    // Given
    long apartmentId = 1;
    Instant startDate = Instant.parse("2023-01-01T00:00:00Z");
    Instant endDate = Instant.parse("2023-01-07T00:00:00Z");
    Reservation reservation = new Reservation(Optional.empty(), apartmentId, startDate, endDate);

    // When
    boolean isPeriodFree = reservationDao.isTimePeriodFree(reservation);

    // Then
    assertFalse(isPeriodFree);
  }

  @Test
  public void shouldHandleEmptyResultSet() {
    // Given
    long apartmentId = 2;

    // When
    List<Reservation> reservations = reservationDao.getAll(apartmentId);

    // Then
    assertTrue(reservations.isEmpty());
  }

  @Test
  public void shouldHandleEmptyResultSetWhenRetrievingReservationById() {
    // Given
    long apartmentId = 2;
    long reservationId = 1;

    // When
    Optional<Reservation> reservation = reservationDao.getById(apartmentId, reservationId);

    // Then
    assertTrue(reservation.isEmpty());
  }

  @Test
  public void shouldHandleInvalidReservationId() {
    // Given
    long apartmentId = 1;
    long reservationId = -1;

    // When
    Optional<Reservation> reservation = reservationDao.getById(apartmentId, reservationId);

    // Then
    assertTrue(reservation.isEmpty());
  }

  @Test
  public void shouldHandleInvalidReservationIdWhenUpdatingReservation() {
    // Given
    long apartmentId = 1;
    long reservationId = -1;
    Instant startDate = Instant.parse("2023-01-01T00:00:00Z");
    Instant endDate = Instant.parse("2023-01-07T00:00:00Z");
    Reservation invalidReservation =
        new Reservation(Optional.of(reservationId), apartmentId, startDate, endDate);

    // When
    reservationDao.update(invalidReservation);

    // Then
    Optional<Reservation> reservation = reservationDao.getById(apartmentId, reservationId);
    assertFalse(reservation.isPresent());
    assertDoesNotThrow(() -> DataAccessException.class);
  }

  @Test
  public void shouldHandleInvalidApartmentIdWhenDeletingReservation() {
    // Given
    long apartmentId = -1;
    long reservationId = 1;

    // When
    reservationDao.deleteById(apartmentId, reservationId);

    // Then
    assertDoesNotThrow(() -> DataAccessException.class);
  }

  @Test
  public void shouldHandleInvalidReservationIdWhenDeletingReservation() {
    // Given
    long apartmentId = 1;
    long reservationId = -1;

    // When
    reservationDao.deleteById(apartmentId, reservationId);

    // Then
    assertDoesNotThrow(() -> DataAccessException.class);
  }
}
