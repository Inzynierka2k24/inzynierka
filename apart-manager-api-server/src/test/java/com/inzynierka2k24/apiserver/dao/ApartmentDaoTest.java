package com.inzynierka2k24.apiserver.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.inzynierka2k24.apiserver.model.Apartment;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.annotation.DirtiesContext;

@JdbcTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ApartmentDaoTest {

  @Autowired private JdbcTemplate template;

  private ApartmentDao apartmentDao;

  @BeforeEach
  void setUp(@Autowired DataSource dataSource) throws SQLException {
    try (Connection connection = dataSource.getConnection()) {
      ScriptUtils.executeSqlScript(connection, new ClassPathResource("db-schema.sql"));
      ScriptUtils.executeSqlScript(connection, new ClassPathResource("apartments-test-data.sql"));
    }

    apartmentDao = new ApartmentDao(template);
  }

  @Test
  void shouldGetAllApartmentsForGivenUserId() {
    // Given
    long userId = 1;
    List<Apartment> expectedApartments = new ArrayList<>();
    expectedApartments.add(
        new Apartment(
            Optional.of(1L),
            100.0f,
            "Apartment 1",
            "Country 1",
            "City 1",
            "Street 1",
            "1",
            "1A",
            4));
    expectedApartments.add(
        new Apartment(
            Optional.of(2L),
            200.0f,
            "Apartment 2",
            "Country 2",
            "City 2",
            "Street 2",
            "2",
            "2B",
            4));

    // When
    List<Apartment> actualApartments = apartmentDao.getAll(userId);

    // Then
    assertEquals(expectedApartments, actualApartments);
  }

  @Test
  void shouldGetApartmentByIdForGivenUserId() {
    // Given
    long userId = 1;
    long apartmentId = 1;
    Apartment expectedApartment =
        new Apartment(
            Optional.of(1L),
            100.0f,
            "Apartment 1",
            "Country 1",
            "City 1",
            "Street 1",
            "1",
            "1A",
            4);

    // When
    Optional<Apartment> actualApartment = apartmentDao.getById(userId, apartmentId);

    // Then
    assertEquals(Optional.of(expectedApartment), actualApartment);
  }

  @Test
  void shouldAddApartmentForGivenUserId() {
    // Given
    long userId = 1;
    long apartmentId = 3;
    Apartment apartment =
        new Apartment(
            Optional.of(apartmentId),
            300.0f,
            "Apartment 3",
            "Country 3",
            "City 3",
            "Street 3",
            "Building 3",
            "Apartment 3",
            4);

    // When
    apartmentDao.add(userId, apartment);

    // Then
    assertEquals(apartmentDao.getById(userId, apartmentId).orElseThrow(), apartment);
  }

  @Test
  void shouldUpdateApartmentForGivenUserId() {
    // Given
    long userId = 1;
    Apartment apartment =
        new Apartment(
            Optional.of(1L),
            200.0f,
            "Apartment 12",
            "Country 12",
            "City 12",
            "Street 12",
            "Building 12",
            "Apartment 12",
            4);

    // When
    apartmentDao.update(userId, apartment);

    // Then
    assertEquals(
        apartmentDao.getById(userId, apartment.id().orElseThrow()).orElseThrow(), apartment);
  }

  @Test
  void shouldDeleteApartmentByIdForGivenUserId() {
    // Given
    long userId = 1;
    long apartmentId = 1;

    // When
    apartmentDao.deleteById(userId, apartmentId);

    // Then
    assertTrue(apartmentDao.getById(userId, apartmentId).isEmpty());
  }

  @Test
  void shouldHandleEmptyResultSetWhenGettingAllApartments() {
    // Given
    long userId = 3;

    // When
    List<Apartment> actualApartments = apartmentDao.getAll(userId);

    // Then
    assertTrue(actualApartments.isEmpty());
  }

  @Test
  void shouldHandleEmptyResultSetWhenGettingApartmentById() {
    // Given
    long userId = 0;
    long apartmentId = 1;

    // When/Then
    assertTrue(apartmentDao.getById(userId, apartmentId).isEmpty());
  }

  @Test
  void shouldHandleMissingUserIdWhenAddingApartment() {
    // Given
    long userId = 0;
    Apartment apartment =
        new Apartment(
            Optional.empty(),
            100.0f,
            "Apartment 1",
            "Country 1",
            "City 1",
            "Street 1",
            "Building 1",
            "Apartment 1",
            4);

    // When/Then
    assertThrows(IllegalArgumentException.class, () -> apartmentDao.add(userId, apartment));
  }
}
