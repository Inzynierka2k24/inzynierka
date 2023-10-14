package com.inzynierka2k24.apiserver.dao;

import com.inzynierka2k24.apiserver.model.User;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDao {

  private final JdbcTemplate template;

  private static final String GET_BY_MAIL_QUERY = "SELECT * FROM users WHERE mail = ?";
  private static final String GET_BY_ID_QUERY = "SELECT * FROM users WHERE user_id = ?";
  private static final String REGISTER_QUERY = "INSERT INTO users VALUES (default, ?, ?)";
  private static final String DELETE_QUERY = "DELETE FROM users WHERE user_id = ?";
  private static final String UPDATE_QUERY =
      """
      UPDATE users
      SET password = ?,
          mail = ?
      WHERE user_id = ?
      """;
  private static final RowMapper<User> userRowMapper =
      (rs, rowNum) ->
          new User(
              Optional.of(rs.getLong("user_id")),
              rs.getString("mail"),
              rs.getString("password"),
              true,
              Set.of("USER"));

  public Optional<User> get(String mail) {
    return Optional.ofNullable(
        DataAccessUtils.singleResult(template.query(GET_BY_MAIL_QUERY, userRowMapper, mail)));
  }

  public Optional<User> get(long userId) {
    return Optional.ofNullable(
        DataAccessUtils.singleResult(template.query(GET_BY_ID_QUERY, userRowMapper, userId)));
  }

  public void register(User user) {
    template.update(REGISTER_QUERY, user.mail(), user.password());
  }

  public void update(User user) {
    template.update(UPDATE_QUERY, user.password(), user.mail(), user.id().orElseThrow());
  }

  public void deleteById(long userId) {
    template.update(DELETE_QUERY, userId);
  }
}
