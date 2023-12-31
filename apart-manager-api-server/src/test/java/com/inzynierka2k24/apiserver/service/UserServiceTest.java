package com.inzynierka2k24.apiserver.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.inzynierka2k24.apiserver.dao.UserDao;
import com.inzynierka2k24.apiserver.exception.user.UserAlreadyExistsException;
import com.inzynierka2k24.apiserver.exception.user.UserNotFoundException;
import com.inzynierka2k24.apiserver.model.User;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserServiceTest {

  private final UserDao userDao = mock(UserDao.class);
  private final UserService userService = new UserService(userDao);

  @Test
  public void shouldAddNewUserToDatabaseWhenUserDoesNotExist() throws UserAlreadyExistsException {
    // Given
    String email = "testUser@gmail.com";
    User user = new User("testUser", email);
    when(userDao.getByEmail(email)).thenReturn(Optional.empty());

    // When
    userService.register(user);

    // Then
    verify(userDao).register(user);
  }

  @Test
  public void shouldUpdateUserInDatabaseWhenUserExists() throws UserNotFoundException {
    // Given
    long id = 2L;
    User user = new User(id, "testUser", "testUser@gmail.com");
    when(userDao.get(id)).thenReturn(Optional.of(user));

    // When
    userService.update(user);

    // Then
    verify(userDao).update(user);
  }

  @Test
  public void shouldDeleteUserFromDatabaseWhenUserExists() throws UserNotFoundException {
    // Given
    long userId = 1L;
    when(userDao.get(userId)).thenReturn(Optional.of(new User(userId, "testUser", "password")));

    // When
    userService.deleteById(userId);

    // Then
    verify(userDao).deleteById(userId);
  }

  @Test
  public void shouldReturnUser() {
    // Given
    String username = "testUser";
    User user = new User(username, "testUser@example.com");
    when(userDao.get(username)).thenReturn(Optional.of(user));

    // When
    final var result = userService.getUser(username);

    // Then
    assertThat(result).isEqualTo(user);
  }

  @Test
  public void shouldThrowUsernameNotFoundExceptionWhenUserDoesNotExist() {
    // Given
    String username = "testUser";

    // When/Then
    assertThrows(UsernameNotFoundException.class, () -> userService.getUser(username));
  }

  @Test
  public void shouldThrowUserAlreadyExistsExceptionWhenUserAlreadyExists() {
    // Given
    String email = "testUser@gmail.com";
    User user = new User("testUser", email);
    when(userDao.getByEmail(email)).thenReturn(Optional.of(user));

    // When/Then
    assertThrows(UserAlreadyExistsException.class, () -> userService.register(user));
  }

  @Test
  public void shouldThrowUserNotFoundExceptionWhenUserDoesNotExist() {
    // Given
    long userId = 1L;
    when(userDao.get(userId)).thenReturn(Optional.empty());

    // When/Then
    assertThrows(UserNotFoundException.class, () -> userService.deleteById(userId));
  }

  @Test
  public void shouldNotRemoveUserThatNotExists() {
    // Given
    long userId = 2L;
    User user = new User(userId, "testUser", "testUser@gmail.com");
    when(userDao.get(userId)).thenReturn(Optional.empty());

    // When/Then
    assertThrows(UserNotFoundException.class, () -> userService.update(user));
  }

  @Test
  public void shouldReturnTrueWhenUserWithGivenMailExists() {
    // Given
    String mail = "test@example.com";
    when(userDao.getByEmail(mail)).thenReturn(Optional.of(new User(mail, "password")));

    // When
    boolean result = userService.existsByMail(mail);

    // Then
    assertTrue(result);
  }

  @Test
  public void shouldReturnFalseWhenUserWithGivenMailDoesNotExist() {
    // Given
    String mail = "test@example.com";
    when(userDao.getByEmail(mail)).thenReturn(Optional.empty());

    // When
    boolean result = userService.existsByMail(mail);

    // Then
    assertFalse(result);
  }

  @Test
  public void shouldReturnTrueWhenUserWithGivenIdExists() {
    // Given
    long userId = 1L;
    when(userDao.get(userId)).thenReturn(Optional.of(new User(userId, "testUser", "password")));

    // When
    boolean result = userService.existsById(userId);

    // Then
    assertTrue(result);
  }

  @Test
  public void shouldReturnFalseWhenUserWithGivenIdDoesNotExist() {
    // Given
    long userId = 1L;
    when(userDao.get(userId)).thenReturn(Optional.empty());

    // When
    boolean result = userService.existsById(userId);

    // Then
    assertFalse(result);
  }
}
