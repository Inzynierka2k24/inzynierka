package com.inzynierka2k24.apiserver.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.inzynierka2k24.apiserver.model.Contact;
import com.inzynierka2k24.apiserver.model.ContactType;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
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
class ContactDaoTest {

  @Autowired private JdbcTemplate template;

  private ContactDao contactDao;

  @BeforeEach
  void setUp(@Autowired DataSource dataSource) throws SQLException {
    try (Connection connection = dataSource.getConnection()) {
      ScriptUtils.executeSqlScript(connection, new ClassPathResource("db-schema.sql"));
      ScriptUtils.executeSqlScript(connection, new ClassPathResource("contacts-test-data.sql"));
    }

    contactDao = new ContactDao(template);
  }

  @Test
  void shouldGetAllContactsForGivenUserId() {
    // Given
    long userId = 1;
    var expectedContacts = new ArrayList<>();
    expectedContacts.add(
        new Contact(
            Optional.of(1L), Optional.of(1L), ContactType.forNumber(1), "Kubol Grubol", "grubol@example.com", "761844591", true, false, 300));
    expectedContacts.add(
        new Contact(
            Optional.of(2L), Optional.of(1L), ContactType.forNumber(2), "Pan Marcin", "marcin.mechanic@example.com", "525701839", false, true, 250));

    // When
    final var result = contactDao.getAll(userId);

    // Then
    assertThat(result)
        .hasSize(expectedContacts.size())
        .isEqualTo(expectedContacts);
  }

  @Test
  void shouldGetContactById() {
    // Given
    long userId = 1;
    long contactId = 1;

    // When
    Optional<Contact> result = contactDao.getById(userId, contactId);

    // Then
    assertThat(result).isPresent();
    assertThat(result.get().id()).isEqualTo(Optional.of(contactId));
  }

  @Test
  void shouldAddNewContact() {
    // Given
    long userId = 1;
    Contact newContact = new Contact(
        Optional.empty(), Optional.of(userId), ContactType.forNumber(3), "New Name", "new@example.com", "123456789", true, false, 150);

    // When
    contactDao.add(userId, newContact);

    // Then
    var result = contactDao.getAll(userId);
    assertThat(result).hasSize(3);
  }

  @Test
  void shouldUpdateContact() {
    // Given
    long userId = 1;
    long contactId = 2;
    Contact updatedContact = new Contact(
        Optional.of(contactId), Optional.of(userId), ContactType.forNumber(2), "Updated Name", "updated@example.com", "987654321", false, true, 200);

    // When
    contactDao.update(userId, updatedContact);

    // Then
    var result = contactDao.getById(userId, contactId);
    assertThat(result).isPresent().hasValue(updatedContact);
  }

  @Test
  void shouldDeleteContactById() {
    // Given
    long userId = 1;
    long contactId = 2;

    // When
    contactDao.deleteById(userId, contactId);

    // Then
    var result = contactDao.getById(userId, contactId);
    assertThat(result).isNotPresent();
  }

  @Test
  void shouldThrowExceptionWhenAddingContactForNonExistentUser() {
    // Given
    long nonExistentUserId = 999;
    Contact newContact = new Contact(
        Optional.empty(), Optional.of(nonExistentUserId), ContactType.forNumber(3), "New Name", "new@example.com", "123456789", true, false, 150);

    // When/Then
    assertThatThrownBy(() -> contactDao.add(nonExistentUserId, newContact))
        .isInstanceOf(IllegalArgumentException.class);
  }
}
