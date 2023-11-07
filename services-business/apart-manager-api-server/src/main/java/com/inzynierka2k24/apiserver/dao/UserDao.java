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

  private static final String GET_BY_MAIL_QUERY = "SELECT * FROM users WHERE mail = ?";
  private static final String GET_BY_ID_QUERY = "SELECT * FROM users WHERE user_id = ?";
  private static final String REGISTER_QUERY = "INSERT INTO users VALUES (default, ?, ?)";
  private static final String DELETE_QUERY = "DELETE FROM users WHERE user_id = ?";
  private static final String UPDATE_QUERY =
      """
                    UPDATE users
                    SET login = ?,
                        mail = ?
                    WHERE user_id = ?
                    """;
  private static final RowMapper<User> userRowMapper =
      (rs, rowNum) ->
          new User(
              Optional.of(rs.getLong("user_id")),
              rs.getString("login"),
              rs.getString("mail"),
              true,
              Set.of("USER"));
  private final JdbcTemplate template;

  public Optional<User> get(String emailAddress) {
    return Optional.ofNullable(
        DataAccessUtils.singleResult(
            template.query(GET_BY_MAIL_QUERY, userRowMapper, emailAddress)));
  }

  public Optional<User> get(long userId) {
    return Optional.ofNullable(
        DataAccessUtils.singleResult(template.query(GET_BY_ID_QUERY, userRowMapper, userId)));
  }

  public void register(User user) {
    template.update(REGISTER_QUERY, user.login(), user.emailAddress());
  }

  public void update(User user) {
    template.update(UPDATE_QUERY, user.login(), user.emailAddress(), user.id().orElseThrow());
  }

  public void deleteById(long userId) {
    template.update(DELETE_QUERY, userId);
  }
}
