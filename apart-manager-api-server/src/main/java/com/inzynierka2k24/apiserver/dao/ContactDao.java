package com.inzynierka2k24.apiserver.dao;

import com.inzynierka2k24.apiserver.model.Contact;
import com.inzynierka2k24.apiserver.model.ContactType;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ContactDao {

  private static final String GET_ALL_QUERY = "SELECT * FROM contacts WHERE user_id = ?";
  private static final String GET_BY_ID_QUERY =
      "SELECT * FROM contacts WHERE user_id = ? and contact_id = ?";
  private static final String ADD_QUERY =
      "INSERT INTO contacts VALUES (default, ?, ?, ?, ?, ?, ?, ?, ?)";
  private static final String DELETE_QUERY =
      "DELETE FROM contacts WHERE user_id = ? and contact_id = ?";
  private static final String UPDATE_QUERY =
      """
        UPDATE contacts
        SET name = ?,
            type = ?,
            mail = ?,
            phone = ?,
            email_notifications = ?,
            sms_notifications = ?,
            price = ?
        WHERE user_id = ? and contact_id = ?
        """;
  private static final RowMapper<Contact> contactRowMapper =
      (rs, rowNum) ->
          new Contact(
              Optional.of(rs.getLong("contact_id")),
              Optional.of(rs.getLong("user_id")),
              ContactType.forNumber(rs.getInt("type")),
              rs.getString("name"),
              rs.getString("mail"),
              rs.getString("phone"),
              rs.getBoolean("email_notifications"),
              rs.getBoolean("sms_notifications"),
              rs.getFloat("price"));
  private final JdbcTemplate template;

  public List<Contact> getAll(long userId) {
    return template.query(GET_ALL_QUERY, contactRowMapper, userId);
  }

  public Optional<Contact> getById(long userId, long contactId) {
    return Optional.ofNullable(
        DataAccessUtils.singleResult(
            template.query(GET_BY_ID_QUERY, contactRowMapper, userId, contactId)));
  }

  public void add(long userId, Contact contact) {
    try {
      template.update(
          ADD_QUERY,
          userId,
          contact.name(),
          contact.contactType().ordinal(),
          contact.mail(),
          contact.phone(),
          contact.emailNotifications(),
          contact.smsNotifications(),
          contact.price());
    } catch (DataIntegrityViolationException e) {
      throw new IllegalArgumentException("The contact doesn't belong to given userId", e);
    }
  }

  public void update(long userId, Contact contact) {
    template.update(
        UPDATE_QUERY,
        contact.name(),
        contact.contactType().ordinal(),
        contact.mail(),
        contact.phone(),
        contact.emailNotifications(),
        contact.smsNotifications(),
        contact.price(),
        userId,
        contact.id().get());
  }

  public void deleteById(long userId, long contactId) {
    template.update(DELETE_QUERY, userId, contactId);
  }
}
