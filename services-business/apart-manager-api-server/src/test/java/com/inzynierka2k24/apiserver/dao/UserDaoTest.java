package com.inzynierka2k24.apiserver.dao;

import static org.junit.jupiter.api.Assertions.*;

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
    assertEquals(email, user.get().emailAddress());
  }

  @Test
  void shouldGetUserById() {
    // Given
    long id = 1;

    // When
    Optional<User> user = userDao.get(id);

    // Then
    assertTrue(user.isPresent());
    assertTrue(user.get().id().isPresent());
    assertEquals(id, user.get().id().get());
  }

  @Test
  void shouldRegisterNewUser() {
    // Given
    User user = new User("test2@example.com", "login");

    // When
    userDao.register(user);
    Optional<User> registeredUser = userDao.get(user.emailAddress());

    // Then
    assertTrue(registeredUser.isPresent());
    assertEquals(user.emailAddress(), registeredUser.get().emailAddress());
    assertEquals(user.login(), registeredUser.get().login());
  }

  @Test
  void shouldUpdateExistingUser() {
    // Given
    User user = new User(1L, "updated@example.com", "login");

    // When
    userDao.update(user);
    Optional<User> updatedUser = userDao.get(user.emailAddress());

    // Then
    assertTrue(updatedUser.isPresent());
    assertEquals(user.emailAddress(), updatedUser.get().emailAddress());
    assertEquals(user.login(), updatedUser.get().login());
  }

  @Test
  void shouldDeleteUserById() {
    // Given
    User user = new User(1L, "test@example.com", "password");

    // When
    userDao.deleteById(user.id().orElseThrow());
    Optional<User> deletedUser = userDao.get(user.emailAddress());

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
