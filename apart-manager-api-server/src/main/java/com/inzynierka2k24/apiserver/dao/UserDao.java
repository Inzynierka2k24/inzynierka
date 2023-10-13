package com.inzynierka2k24.apiserver.dao;

import com.inzynierka2k24.apiserver.model.User;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDao {
  private final JdbcTemplate template;

  public Optional<User> get(String mail, String password) {
    return Optional.ofNullable(
        DataAccessUtils.singleResult(
            template.query(
                "SELECT * FROM users WHERE mail = ? and password = ?",
                this::userRowMapper,
                mail,
                password)));
  }

  public Optional<User> get(String mail) {
    return Optional.ofNullable(
        DataAccessUtils.singleResult(
            template.query("SELECT * FROM users WHERE mail = ?", this::userRowMapper, mail)));
  }

  public void register(User user) {
    template.update("INSERT INTO users VALUES (default, ?, ?)", user.mail(), user.password());
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
        rs.getString("mail"),
        rs.getString("password"),
        true,
        Set.of("USER"));
  }
}
