package com.inzynierka2k24.apiserver.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.inzynierka2k24.apiserver.model.Finance;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.Instant;
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
class FinanceDaoTest {

  @Autowired private JdbcTemplate template;

  private FinanceDao financeDao;

  @BeforeEach
  void setUp(@Autowired DataSource dataSource) throws SQLException {
    try (Connection connection = dataSource.getConnection()) {
      ScriptUtils.executeSqlScript(connection, new ClassPathResource("db-schema.sql"));
      ScriptUtils.executeSqlScript(connection, new ClassPathResource("finances-test-data.sql"));
    }

    financeDao = new FinanceDao(template);
  }

  @Test
  void shouldGetAllFinancesForGivenUserId() {
    // Given
    long userId = 1;
    List<Finance> expectedFinances = new ArrayList<>();
    expectedFinances.add(
        new Finance(
            Optional.of(1L),
            1L,
            1L,
            1,
            1,
            200.5f,
            Instant.parse("2023-01-01T00:00:00Z"),
            "Washing machine repair"));
    expectedFinances.add(
        new Finance(
            Optional.of(2L), 1L, 1L, 1, 1, 200.5f, Instant.parse("2023-01-01T00:00:00Z"), "none"));

    // When
    List<Finance> actualFinances = financeDao.getAll(userId);
    // Then
    assertEquals(expectedFinances, actualFinances);
  }

  @Test
  void shouldGetFinanceById() {
    // Given
    long financeId = 1;
    Finance expectedFinance =
        new Finance(
            Optional.of(1L),
            1L,
            1L,
            1,
            1,
            200.5f,
            Instant.parse("2023-01-01T00:00:00Z"),
            "Washing machine repair");

    // When
    Optional<Finance> actualFinance = financeDao.getById(financeId);

    // Then
    assertEquals(Optional.of(expectedFinance), actualFinance);
  }

  @Test
  void shouldGetFinancesForGivenApartmentId() {
    // Given
    long apartmentId = 1L;
    List<Finance> expectedFinances = new ArrayList<>();
    expectedFinances.add(
        new Finance(
            Optional.of(1L),
            1L,
            1L,
            1,
            1,
            200.5f,
            Instant.parse("2023-01-01T00:00:00Z"),
            "Washing machine repair"));
    expectedFinances.add(
        new Finance(
            Optional.of(2L), 1L, 1L, 1, 1, 200.5f, Instant.parse("2023-01-01T00:00:00Z"), "none"));

    // When
    List<Finance> actualFinance = financeDao.getByApartmentId(apartmentId);

    // Then
    assertEquals(expectedFinances, actualFinance);
  }

  @Test
  void shouldAddFinance() {
    // Given
    long financeId = 3;
    Finance finance =
        new Finance(
            Optional.of(financeId),
            1L,
            1L,
            1,
            1,
            200.5f,
            Instant.parse("2023-01-01T00:00:00Z"),
            "Washing machine repair");

    // When
    financeDao.add(finance);

    // Then
    assertEquals(financeDao.getById(financeId).orElseThrow(), finance);
  }

  @Test
  void shouldUpdateFinanceForGivenFinanceId() {
    // Given
    Finance finance =
        new Finance(
            Optional.of(1L),
            1L,
            1L,
            1,
            1,
            333f,
            Instant.parse("2023-01-01T00:00:00Z"),
            "Washing machine repair");

    // When
    financeDao.update(finance);

    // Then
    assertEquals(financeDao.getById(finance.id().orElseThrow()).orElseThrow(), finance);
  }

  @Test
  void shouldDeleteFinanceById() {
    // Given
    long financeId = 1;

    // When
    financeDao.deleteById(financeId);

    // Then
    assertTrue(financeDao.getById(financeId).isEmpty());
  }

  @Test
  void shouldHandleEmptyResultSetWhenGettingAllFinances() {
    // Given
    long userId = 13;

    // When
    List<Finance> actualFinances = financeDao.getAll(userId);

    // Then
    assertTrue(actualFinances.isEmpty());
  }

  @Test
  void shouldHandleEmptyResultSetWhenGettingFinanceById() {
    // Given
    long financeId = 100;

    // When/Then
    assertTrue(financeDao.getById(financeId).isEmpty());
  }

  @Test
  void shouldHandleMissingUserIdWhenAddingFinance() {
    // Given
    Finance finance =
        new Finance(
            Optional.empty(),
            100L,
            1L,
            1,
            1,
            200.5f,
            Instant.parse("2023-01-01T00:00:00Z"),
            "Washing machine repair");

    // When/Then
    assertThrows(IllegalArgumentException.class, () -> financeDao.add(finance));
  }

  @Test
  void shouldHandleMissingApartmentIdWhenAddingFinance() {
    // Given
    Finance finance =
        new Finance(
            Optional.empty(),
            1L,
            100L,
            1,
            1,
            200.5f,
            Instant.parse("2023-01-01T00:00:00Z"),
            "Washing machine repair");

    // When/Then
    assertThrows(IllegalArgumentException.class, () -> financeDao.add(finance));
  }

  @Test
  void shouldHandleMissingUserIdWhenUpdatingFinance() {
    // Given
    Finance finance =
        new Finance(
            Optional.of(1L),
            100L,
            1L,
            1,
            1,
            200.5f,
            Instant.parse("2023-01-01T00:00:00Z"),
            "Washing machine repair");

    // When/Then
    assertThrows(IllegalArgumentException.class, () -> financeDao.update(finance));
  }

  @Test
  void shouldHandleMissingApartmentIdWhenUpdatingFinance() {
    // Given
    Finance finance =
        new Finance(
            Optional.of(1L),
            1L,
            100L,
            1,
            1,
            200.5f,
            Instant.parse("2023-01-01T00:00:00Z"),
            "Washing machine repair");

    // When/Then
    assertThrows(IllegalArgumentException.class, () -> financeDao.update(finance));
  }

  @Test
  void shouldHandleMissingFinanceIdWhenUpdatingFinance() {
    // Given
    Finance finance =
        new Finance(
            Optional.empty(),
            1L,
            1L,
            1,
            1,
            200.5f,
            Instant.parse("2023-01-01T00:00:00Z"),
            "Washing machine repair");

    // When/Then
    assertThrows(IllegalArgumentException.class, () -> financeDao.update(finance));
  }
}
