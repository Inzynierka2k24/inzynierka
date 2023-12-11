package com.inzynierka2k24.apiserver.dao;

import static org.assertj.core.api.Assertions.assertThat;

import com.inzynierka2k24.apiserver.model.IntervalType;
import com.inzynierka2k24.apiserver.model.ScheduledMessage;
import com.inzynierka2k24.apiserver.model.TriggerType;
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
class ScheduledMessageDaoTest {

  @Autowired private JdbcTemplate template;

  private ScheduledMessageDao scheduledMessageDao;

  @BeforeEach
  void setUp(@Autowired DataSource dataSource) throws SQLException {
    try (Connection connection = dataSource.getConnection()) {
      ScriptUtils.executeSqlScript(connection, new ClassPathResource("db-schema.sql"));
      ScriptUtils.executeSqlScript(connection, new ClassPathResource("scheduled-message-test-data.sql"));
    }

    scheduledMessageDao = new ScheduledMessageDao(template);
  }

  @Test
  void shouldGetAllScheduledMessagesForUser() {
    // Given
    long userId = 1;
    long contactId = 1;

    // When
    List<ScheduledMessage> messages = scheduledMessageDao.getAllForUser(userId, contactId);

    // Then
    assertThat(messages).isNotEmpty();
  }

  @Test
  void shouldGetApartmentsForMessage() {
    // Given
    long messageId = 1;

    // When
    List<Long> apartments = scheduledMessageDao.getApartmentsForMessage(messageId);

    // Then
    assertThat(apartments).isNotEmpty();
  }

  @Test
  void shouldAddScheduledMessage() {
    // Given
    long userId = 1;
    long contactId = 1;
    long apartmentId = 1;
    ScheduledMessage message =
        new ScheduledMessage(Optional.empty(), userId, contactId, "New Message",
            IntervalType.forNumber(2), 15, TriggerType.forNumber(2));

    // When
    scheduledMessageDao.add(userId, apartmentId, message);

    // Then
    List<ScheduledMessage> messages = scheduledMessageDao.getAllForUser(userId, contactId);
    assertThat(messages).hasSize(2);
  }

  @Test
  void shouldDeleteScheduledMessageById() {
    // Given
    long userId = 1;
    long contactId = 1;
    long messageId = 1;

    // When
    scheduledMessageDao.deleteById(userId, contactId, messageId);

    // Then
    List<ScheduledMessage> messages = scheduledMessageDao.getAllForUser(userId, contactId);
    assertThat(messages).isEmpty();
  }
}
