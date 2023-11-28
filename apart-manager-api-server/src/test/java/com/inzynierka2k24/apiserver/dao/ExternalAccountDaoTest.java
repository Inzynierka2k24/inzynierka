package com.inzynierka2k24.apiserver.dao;

import static com.inzynierka2k24.ExternalService.AIRBNB;
import static com.inzynierka2k24.ExternalService.BOOKING;
import static org.junit.jupiter.api.Assertions.*;

import com.inzynierka2k24.apiserver.model.ExternalAccount;
import java.sql.Connection;
import java.sql.SQLException;
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
class ExternalAccountDaoTest {

  @Autowired private JdbcTemplate template;

  private ExternalAccountDao externalAccountDao;

  @BeforeEach
  void setUp(@Autowired DataSource dataSource) throws SQLException {
    try (Connection connection = dataSource.getConnection()) {
      ScriptUtils.executeSqlScript(connection, new ClassPathResource("db-schema.sql"));
      ScriptUtils.executeSqlScript(connection, new ClassPathResource("accounts-test-data.sql"));
    }

    externalAccountDao = new ExternalAccountDao(template);
  }

  @Test
  void shouldGetAllAccountsForGivenUserId() {
    // Given
    long userId = 1;
    List<ExternalAccount> expectedAccounts =
        List.of(
            new ExternalAccount(Optional.of(1L), "test", "test", AIRBNB),
            new ExternalAccount(Optional.of(2L), "test2", "test2", BOOKING));

    // When
    List<ExternalAccount> actualAccounts = externalAccountDao.getAll(userId);

    // Then
    assertEquals(expectedAccounts, actualAccounts);
  }

  @Test
  void shouldGetAccountByIdForGivenUserId() {
    // Given
    long userId = 1;
    long apartmentId = 1;
    ExternalAccount expectedAccount = new ExternalAccount(Optional.of(1L), "test", "test", AIRBNB);

    // When
    Optional<ExternalAccount> actualAccount = externalAccountDao.getById(userId, apartmentId);

    // Then
    assertEquals(Optional.of(expectedAccount), actualAccount);
  }

  @Test
  void shouldAddAccountForGivenUserId() {
    // Given
    long userId = 1;
    long accountId = 1;
    ExternalAccount account = new ExternalAccount(Optional.of(1L), "test", "test", AIRBNB);

    // When
    externalAccountDao.add(userId, account);

    // Then
    assertEquals(externalAccountDao.getById(userId, accountId).orElseThrow(), account);
  }

  @Test
  void shouldUpdateAccountForGivenUserId() {
    // Given
    long userId = 1;
    ExternalAccount account = new ExternalAccount(Optional.of(1L), "test3", "test3", AIRBNB);

    // When
    externalAccountDao.update(userId, account);

    // Then
    assertEquals(
        externalAccountDao.getById(userId, account.id().orElseThrow()).orElseThrow(), account);
  }

  @Test
  void shouldDeleteAccountByIdForGivenUserId() {
    // Given
    long userId = 1;
    long accountId = 1;

    // When
    externalAccountDao.deleteById(userId, accountId);

    // Then
    assertTrue(externalAccountDao.getById(userId, accountId).isEmpty());
  }

  @Test
  void shouldHandleEmptyResultSetWhenGettingAllAccounts() {
    // Given
    long userId = 3;

    // When
    List<ExternalAccount> actualAccounts = externalAccountDao.getAll(userId);

    // Then
    assertTrue(actualAccounts.isEmpty());
  }

  @Test
  void shouldHandleEmptyResultSetWhenGettingAccountById() {
    // Given
    long userId = 0;
    long apartmentId = 1;

    // When/Then
    assertTrue(externalAccountDao.getById(userId, apartmentId).isEmpty());
  }

  @Test
  void shouldHandleMissingUserIdWhenAddingAccount() {
    // Given
    long userId = 0;
    ExternalAccount account = new ExternalAccount(Optional.empty(), "test", "test", BOOKING);

    // When/Then
    assertThrows(IllegalArgumentException.class, () -> externalAccountDao.add(userId, account));
  }
}
