package com.inzynierka2k24.apiserver.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.inzynierka2k24.apiserver.model.User;
import java.sql.Connection;
import java.sql.SQLException;
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
class UserDaoTest {

  @Autowired private JdbcTemplate template;

  private UserDao userDao;

  @BeforeEach
  void setUp(@Autowired DataSource dataSource) throws SQLException {
    try (Connection connection = dataSource.getConnection()) {
      ScriptUtils.executeSqlScript(connection, new ClassPathResource("db-schema.sql"));
      ScriptUtils.executeSqlScript(connection, new ClassPathResource("users-test-data.sql"));
    }

    userDao = new UserDao(template);
  }

  @Test
  void shouldGetUserByMail() {
    // Given
    String email = "test@example.com";

    // When
    Optional<User> user = userDao.get(email);

    // Then
    assertTrue(user.isPresent());
    assertEquals(email, user.get().mail());
  }

  @Test
  void shouldRegisterNewUser() {
    // Given
    User user = new User("test2@example.com", "password");

    // When
    userDao.register(user);
    Optional<User> registeredUser = userDao.get(user.mail());

    // Then
    assertTrue(registeredUser.isPresent());
    assertEquals(user.mail(), registeredUser.get().mail());
    assertEquals(user.password(), registeredUser.get().password());
  }

  @Test
  void shouldUpdateExistingUser() {
    // Given
    User user = new User(1L, "updated@example.com", "new_password");

    // When
    userDao.update(user);
    Optional<User> updatedUser = userDao.get(user.mail());

    // Then
    assertTrue(updatedUser.isPresent());
    assertEquals(user.mail(), updatedUser.get().mail());
    assertEquals(user.password(), updatedUser.get().password());
  }

  @Test
  void shouldDeleteUserById() {
    // Given
    User user = new User(1L, "test@example.com", "password");

    // When
    userDao.deleteById(user.id().orElseThrow());
    Optional<User> deletedUser = userDao.get(user.mail());

    // Then
    assertFalse(deletedUser.isPresent());
  }

  @Test
  void shouldGetEmptyOptionalWhenNonexistentUser() {
    // Given
    String email = "nonexistent@example.com";

    // When
    Optional<User> user = userDao.get(email);

    // Then
    assertFalse(user.isPresent());
  }
}
