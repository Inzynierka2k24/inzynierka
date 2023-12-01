package com.inzynierka2k24.apiserver.dao;

import static java.sql.Timestamp.from;

import com.inzynierka2k24.apiserver.model.Finance;
import java.time.ZoneOffset;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FinanceDao {

  private static final String GET_ALL_QUERY = "SELECT * FROM finances WHERE user_id = ?";
  private static final String GET_BY_ID_QUERY = "SELECT * FROM finances WHERE finance_id = ?";
  private static final String GET_BY_APARTMENT_QUERY =
      "SELECT * FROM finances WHERE apartment_id = ?";
  private static final String ADD_QUERY =
      "INSERT INTO finances VALUES (default, ?, ?, ?, ?, ?, ?, ?, ?)";
  private static final String DELETE_QUERY = "DELETE FROM finances WHERE finance_id = ?";
  private static final String UPDATE_QUERY =
      """
        UPDATE
          finances
        SET
          user_id = ?,
          apartment_id = ?,
          event_id = ?,
          event_type = ?,
          source = ?,
          price = ?,
          date = ?,
          details = ?
        WHERE
          finance_id = ?
        """;

  private static final RowMapper<Finance> financeRowMapper =
      (rs, rowNum) ->
          new Finance(
              Optional.of(rs.getLong("finance_id")),
              rs.getLong("user_id"),
              rs.getLong("apartment_id"),
              rs.getLong("event_id"),
              rs.getInt("event_type"),
              rs.getInt("source"),
              rs.getFloat("price"),
              rs.getTimestamp("date").toLocalDateTime().toInstant(ZoneOffset.UTC),
              rs.getString("details"));
  private final JdbcTemplate template;

  public List<Finance> getAll(long userId) {
    return template.query(GET_ALL_QUERY, financeRowMapper, userId);
  }

  public Optional<Finance> getById(long financeId) {
    return Optional.ofNullable(
        DataAccessUtils.singleResult(template.query(GET_BY_ID_QUERY, financeRowMapper, financeId)));
  }

  public List<Finance> getByApartmentId(long apartmentId) {
    return template.query(GET_BY_APARTMENT_QUERY, financeRowMapper, apartmentId);
  }

  public void add(Finance finance) {
    try {
      template.update(
          ADD_QUERY,
          finance.userId(),
          finance.apartmentId(),
          finance.eventId(),
          finance.eventType().ordinal(),
          finance.source().ordinal(),
          finance.price(),
          from(finance.date()),
          finance.details());
    } catch (DataIntegrityViolationException e) {
      throw new IllegalArgumentException("The given userId or apartmentId doesn't exist", e);
    }
  }

  public void update(Finance finance) {
    try {
      template.update(
          UPDATE_QUERY,
          finance.userId(),
          finance.apartmentId(),
          finance.eventId(),
          finance.eventType().ordinal(),
          finance.source().ordinal(),
          finance.price(),
          finance.date(),
          finance.details(),
          finance.id().orElseThrow());
    } catch (DataAccessException e) {
      throw new IllegalArgumentException("The given userId or apartmentId doesn't exist", e);
    } catch (NoSuchElementException e) {
      throw new IllegalArgumentException("The given financeId doesn't exist", e);
    }
  }

  public void deleteById(long financeId) {
    template.update(DELETE_QUERY, financeId);
  }
}
