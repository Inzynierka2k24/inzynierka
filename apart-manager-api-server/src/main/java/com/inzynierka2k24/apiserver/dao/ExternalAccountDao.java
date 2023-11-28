package com.inzynierka2k24.apiserver.dao;

import com.inzynierka2k24.ExternalService;
import com.inzynierka2k24.apiserver.model.ExternalAccount;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExternalAccountDao {

  private final JdbcTemplate template;

  private static final String GET_ALL_QUERY = "SELECT * FROM external_accounts WHERE user_id = ?";
  private static final String GET_BY_ID_QUERY =
      "SELECT * FROM external_accounts WHERE user_id = ? and account_id = ?";
  private static final String ADD_QUERY =
      "INSERT INTO external_accounts VALUES (default, ?, ?, ?, ?)";
  private static final String DELETE_QUERY =
      "DELETE FROM external_accounts WHERE user_id = ? and account_id = ?";
  private static final String UPDATE_QUERY =
      """
      UPDATE external_accounts
      SET login = ?,
          password = ?,
          service_type = ?
      WHERE user_id = ? and account_id = ?
      """;
  private static final RowMapper<ExternalAccount> accountRowMapper =
      (rs, rowNum) ->
          new ExternalAccount(
              Optional.of(rs.getLong("account_id")),
              rs.getString("login"),
              rs.getString("password"),
              ExternalService.forNumber(rs.getInt("service_type")));

  public List<ExternalAccount> getAll(long userId) {
    return template.query(GET_ALL_QUERY, accountRowMapper, userId);
  }

  public Optional<ExternalAccount> getById(long userId, long apartmentId) {
    return Optional.ofNullable(
        DataAccessUtils.singleResult(
            template.query(GET_BY_ID_QUERY, accountRowMapper, userId, apartmentId)));
  }

  public void add(long userId, ExternalAccount account) {
    try {
      template.update(
          ADD_QUERY,
          userId,
          account.login(),
          account.password(),
          account.serviceType().getNumber());
    } catch (DataIntegrityViolationException e) {
      throw new IllegalArgumentException("The account doesn't belong to given userId", e);
    }
  }

  public void update(long userId, ExternalAccount account) {
    template.update(
        UPDATE_QUERY,
        account.login(),
        account.password(),
        account.serviceType().getNumber(),
        userId,
        account.id().orElseThrow());
  }

  public void deleteById(long userId, long accountId) {
    template.update(DELETE_QUERY, userId, accountId);
  }
}
