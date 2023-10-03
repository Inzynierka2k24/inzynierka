package com.inzynierka2k24.apiserver.dao;

import com.inzynierka2k24.apiserver.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDao {
  private final JdbcTemplate template;

  public Optional<User> get(String login, String password) {
    return Optional.ofNullable(
        DataAccessUtils.singleResult(
            template.query(
                "SELECT * FROM users WHERE login = ? and password = ?",
                this::userRowMapper,
                login,
                password)));
  }

  public void register(User user) {
    template.update(
        "INSERT INTO users VALUES (default, ?, ?, ?)", user.login(), user.password(), user.mail());
  }

  public void update(User user) {
    var query =
        """
      UPDATE users
      SET password = ?,
          mail = ?,
      WHERE user_id = ?
      """;
    template.update(query, user.password(), user.mail(), user.id().orElseThrow());
  }

  public void deleteById(long userId) {
    template.update("DELETE FROM users WHERE user_id = ?", userId);
  }

  private User userRowMapper(ResultSet rs, int rowNum) throws SQLException {
    return new User(
        Optional.of(rs.getLong("user_id")),
        rs.getString("login"),
        rs.getString("password"),
        rs.getString("mail"),
        true,
        List.of("USER"));
  }
}
