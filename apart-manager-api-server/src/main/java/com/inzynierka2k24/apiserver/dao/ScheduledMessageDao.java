package com.inzynierka2k24.apiserver.dao;

import com.inzynierka2k24.apiserver.model.IntervalType;
import com.inzynierka2k24.apiserver.model.ScheduledMessage;
import com.inzynierka2k24.apiserver.model.TriggerType;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScheduledMessageDao {
  private static final String GET_ALL_FOR_USER_QUERY =
      "SELECT * FROM scheduled_messages WHERE user_id = ? AND contact_id = ?";
  private static final String ADD_QUERY =
      "INSERT INTO scheduled_messages VALUES (default, ?, ?, ?, ?, ?, ?, ?)";

  private static final RowMapper<ScheduledMessage> scheduledMessageRowMapper =
      (rs, rowNum) ->
          new ScheduledMessage(
              Optional.of(rs.getLong("message_id")),
              rs.getLong("user_id"),
              rs.getLong("contact_id"),
              rs.getLong("apartment_id"),
              rs.getString("message"),
              IntervalType.forNumber(rs.getInt("interval_type")),
              rs.getInt("interval_value"),
              TriggerType.forNumber(rs.getInt("trigger_type")));
  private final JdbcTemplate template;

  public List<ScheduledMessage> getAllForUser(long userId, long contactId) {
    return template.query(GET_ALL_FOR_USER_QUERY, scheduledMessageRowMapper, userId, contactId);
  }

  public void add(long userId, ScheduledMessage message) {
    template.update(
        ADD_QUERY,
        userId,
        message.contactId(),
        message.apartmentId(),
        message.message(),
        message.intervalType().ordinal(),
        message.intervalValue(),
        message.triggerType().ordinal());
  }
}
