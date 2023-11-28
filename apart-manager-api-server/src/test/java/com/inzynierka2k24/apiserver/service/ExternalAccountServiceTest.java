package com.inzynierka2k24.apiserver.service;

import static com.inzynierka2k24.apiserver.model.ServiceType.AIRBNB;
import static com.inzynierka2k24.apiserver.model.ServiceType.BOOKING;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.inzynierka2k24.apiserver.dao.ExternalAccountDao;
import com.inzynierka2k24.apiserver.exception.account.AccountNotFoundException;
import com.inzynierka2k24.apiserver.model.ExternalAccount;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

public class ExternalAccountServiceTest {

  private final ExternalAccountDao accountDao = mock(ExternalAccountDao.class);
  private final ExternalAccountService accountService = new ExternalAccountService(accountDao);

  @Test
  public void shouldGetAllAccountsForUserId() {
    // Given
    long userId = 1;
    List<ExternalAccount> expectedAccounts =
        List.of(
            new ExternalAccount(Optional.of(1L), "test", "test", AIRBNB),
            new ExternalAccount(Optional.of(2L), "test2", "test2", BOOKING));
    when(accountDao.getAll(userId)).thenReturn(expectedAccounts);

    // When
    List<ExternalAccount> actualAccounts = accountService.getAll(userId);

    // Then
    assertEquals(expectedAccounts, actualAccounts);
  }

  @Test
  public void shouldGetAccountByIdForUserId() throws AccountNotFoundException {
    // Given
    long userId = 1;
    long accountId = 1;
    ExternalAccount expectedAccount = new ExternalAccount(Optional.of(1L), "test", "test", AIRBNB);
    when(accountDao.getById(userId, accountId)).thenReturn(Optional.of(expectedAccount));

    // When
    ExternalAccount actualAccount = accountService.getById(userId, accountId);

    // Then
    assertEquals(expectedAccount, actualAccount);
  }

  @Test
  public void shouldAddAccountForUserId() {
    // Given
    long userId = 1;
    ExternalAccount account = new ExternalAccount(Optional.of(1L), "test", "test", AIRBNB);

    // When
    accountService.add(userId, account);

    // Then
    verify(accountDao).add(userId, account);
  }

  @Test
  public void shouldUpdateExistingAccountForUserIdAndAccountId() throws AccountNotFoundException {
    // Given
    long userId = 1;
    long AccountId = 1;
    ExternalAccount account = new ExternalAccount(Optional.of(1L), "test", "test", AIRBNB);
    when(accountDao.getById(userId, AccountId)).thenReturn(Optional.of(account));

    // When
    accountService.update(userId, account);

    // Then
    verify(accountDao).update(userId, account);
  }

  @Test
  public void shouldDeleteExistingAccountForUserIdAndAccountId() throws AccountNotFoundException {
    // Given
    long userId = 1;
    long accountId = 1;
    when(accountDao.getById(userId, accountId))
        .thenReturn(Optional.of(new ExternalAccount(Optional.of(1L), "test", "test", AIRBNB)));

    // When
    accountService.deleteById(userId, accountId);

    // Then
    verify(accountDao).deleteById(userId, accountId);
  }

  @Test
  public void
      shouldThrowAccountNotFoundExceptionWhenGettingNonExistingAccountForUserIdAndAccountId() {
    // Given
    long userId = 1;
    long accountId = 1;
    when(accountDao.getById(userId, accountId)).thenReturn(Optional.empty());

    // When/Then
    assertThrows(AccountNotFoundException.class, () -> accountService.getById(userId, accountId));
  }

  @Test
  public void shouldThrowIllegalArgumentExceptionWhenAddingAccountNotBelongingToUserId() {
    // Given
    long userId = 1;
    ExternalAccount account = new ExternalAccount(Optional.of(1L), "test", "test", AIRBNB);
    doThrow(new IllegalArgumentException()).when(accountDao).add(userId, account);

    // When/Then
    assertThrows(IllegalArgumentException.class, () -> accountService.add(userId, account));
  }

  @Test
  public void
      shouldThrowAccountNotFoundExceptionWhenUpdatingNonExistingAccountForUserIdAndAccountId() {
    // Given
    long userId = 1;
    long accountId = 1;
    ExternalAccount account = new ExternalAccount(Optional.of(1L), "test", "test", AIRBNB);
    when(accountDao.getById(userId, accountId)).thenReturn(Optional.empty());

    // When/Then
    assertThrows(AccountNotFoundException.class, () -> accountService.update(userId, account));
  }

  @Test
  public void
      shouldThrowAccountNotFoundExceptionWhenDeletingNonExistingAccountForUserIdAndAccountId() {
    // Given
    long userId = 1;
    long accountId = 1;
    when(accountDao.getById(userId, accountId)).thenReturn(Optional.empty());

    // When/Then
    assertThrows(
        AccountNotFoundException.class, () -> accountService.deleteById(userId, accountId));
  }

  @Test
  public void testAccountExistsForUserIdAndAccountId() {
    // Given
    long userId = 1;
    long accountId = 1;
    when(accountDao.getById(userId, accountId))
        .thenReturn(Optional.of(new ExternalAccount(Optional.of(1L), "test", "test", AIRBNB)));

    // When
    boolean exists = accountService.existsById(userId, accountId);

    // Then
    assertTrue(exists);
  }

  @Test
  public void testAccountDoesNotExistForUserIdAndAccountId() {
    // Given
    long userId = 1;
    long accountId = 1;
    when(accountDao.getById(userId, accountId)).thenReturn(Optional.empty());

    // When
    boolean exists = accountService.existsById(userId, accountId);

    // Then
    assertFalse(exists);
  }

  @Test
  public void shouldNotUpdateAccountIfIdNotPresent() {
    // Given
    long userId = 1;
    ExternalAccount account = new ExternalAccount(Optional.of(1L), "test", "test", AIRBNB);

    // When
    assertThrows(AccountNotFoundException.class, () -> accountService.update(userId, account));

    // Then
    verify(accountDao, never()).update(anyLong(), any(ExternalAccount.class));
  }
}
