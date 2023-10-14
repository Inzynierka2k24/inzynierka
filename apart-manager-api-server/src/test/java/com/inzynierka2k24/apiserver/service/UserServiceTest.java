package com.inzynierka2k24.apiserver.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.inzynierka2k24.apiserver.dao.UserDao;
import com.inzynierka2k24.apiserver.exception.user.UserAlreadyExistsException;
import com.inzynierka2k24.apiserver.exception.user.UserNotFoundException;
import com.inzynierka2k24.apiserver.model.User;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserServiceTest {

  private final UserDao userDao = mock(UserDao.class);
  private final UserService userService = new UserService(userDao);

  @Test
  public void shouldReturnUserDetailsWhenUserExists() {
    // Given
    String username = "testUser";
    User user = new User(username, "password");
    when(userDao.get(username)).thenReturn(Optional.of(user));

    // When
    UserDetails result = userService.loadUserByUsername(username);

    // Then
    assertNotNull(result);
    assertEquals(username, result.getUsername());
  }

  @Test
  public void shouldAddNewUserToDatabaseWhenUserDoesNotExist() throws UserAlreadyExistsException {
    // Given
    String username = "testUser";
    User user = new User(username, "password");
    when(userDao.get(username)).thenReturn(Optional.empty());

    // When
    userService.register(user);

    // Then
    verify(userDao).register(user);
  }

  @Test
  public void shouldUpdateUserInDatabaseWhenUserExists() throws UserNotFoundException {
    // Given
    String username = "testUser";
    User user = new User(username, "password");
    when(userDao.get(username)).thenReturn(Optional.of(user));

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
  public void shouldThrowUsernameNotFoundExceptionWhenUserDoesNotExist() {
    // Given
    String username = "testUser";
    when(userDao.get(username)).thenReturn(Optional.empty());

    // When/Then
    assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername(username));
  }

  @Test
  public void shouldThrowUserAlreadyExistsExceptionWhenUserAlreadyExists() {
    // Given
    String username = "testUser";
    User user = new User(username, "password");
    when(userDao.get(username)).thenReturn(Optional.of(user));

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
    String username = "testUser";
    User user = new User(username, "password");
    when(userDao.get(username)).thenReturn(Optional.empty());

    // When/Then
    assertThrows(UserNotFoundException.class, () -> userService.update(user));
  }

  @Test
  public void shouldReturnTrueWhenUserWithGivenMailExists() {
    // Given
    String mail = "test@example.com";
    when(userDao.get(mail)).thenReturn(Optional.of(new User(mail, "password")));

    // When
    boolean result = userService.existsByMail(mail);

    // Then
    assertTrue(result);
  }

  @Test
  public void shouldReturnFalseWhenUserWithGivenMailDoesNotExist() {
    // Given
    String mail = "test@example.com";
    when(userDao.get(mail)).thenReturn(Optional.empty());

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

  @Test
  public void shouldReturnUserDetailsWithCorrectAuthorities() {
    // Given
    String username = "testUser";
    User user = new User(username, "password");
    when(userDao.get(username)).thenReturn(Optional.of(user));

    // When
    UserDetails result = userService.loadUserByUsername(username);

    // Then
    assertNotNull(result);
    assertEquals(username, result.getUsername());
    assertTrue(result.getAuthorities().contains(new SimpleGrantedAuthority("USER")));
  }
}
