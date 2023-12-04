package com.inzynierka2k24.apiserver.dao;

import com.inzynierka2k24.apiserver.model.IntervalType;
import com.inzynierka2k24.apiserver.model.ScheduledMessage;
import com.inzynierka2k24.apiserver.model.TriggerType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ScheduledMessageDao {
  private static final String GET_ALL_FOR_USER_QUERY =
      "SELECT * FROM scheduled_messages WHERE user_id = ? AND contact_id = ?";
  private static final String BIND_APARTMENT_QUERY =
      "INSERT INTO scheduled_messages_apartments VALUES (?, ?)";

  private static final String GET_APARTMENTS_FOR_MESSAGE_QUERY =
      "SELECT apartment_id FROM scheduled_messages_apartments WHERE message_id = ?";

  private static final RowMapper<ScheduledMessage> scheduledMessageRowMapper =
      (rs, rowNum) ->
          new ScheduledMessage(
              Optional.of(rs.getLong("message_id")),
              rs.getLong("user_id"),
              rs.getLong("contact_id"),
              rs.getString("message"),
              IntervalType.forNumber(rs.getInt("interval_type")),
              rs.getInt("interval_value"),
              TriggerType.forNumber(rs.getInt("trigger_type")));
  private final JdbcTemplate template;

  public List<ScheduledMessage> getAllForUser(long userId, long contactId) {
    return template.query(GET_ALL_FOR_USER_QUERY, scheduledMessageRowMapper, userId, contactId);
  }

  public List<Long> getApartmentsForMessage(long messageId) {
    return template.queryForList(GET_APARTMENTS_FOR_MESSAGE_QUERY, Long.class, messageId);
  }

  public void add(long userId, long apartmentId, ScheduledMessage message) {
    SimpleJdbcInsert insertIntoUser =
        new SimpleJdbcInsert(template)
            .withTableName("scheduled_messages")
            .usingGeneratedKeyColumns("message_id");
    final Map<String, Object> params = new HashMap<>();
    params.put("user_id", userId);
    params.put("contact_id", message.contactId());
    params.put("message", message.message());
    params.put("interval_type", message.intervalType().getNumber());
    params.put("interval_value", message.intervalValue());
    params.put("trigger_type", message.triggerType().getNumber());

    long id = insertIntoUser.executeAndReturnKey(params).longValue();

    template.update(BIND_APARTMENT_QUERY, id, apartmentId);
  }

  public void deleteById(long userId, long contactId, long messageId) {
    template.update(
        "DELETE FROM scheduled_messages WHERE user_id = ? AND contact_id = ? AND message_id = ?",
        userId,
        contactId,
        messageId);
  }
}
