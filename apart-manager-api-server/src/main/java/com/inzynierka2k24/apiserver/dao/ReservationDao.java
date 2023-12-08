package com.inzynierka2k24.apiserver.dao;

import static java.sql.Timestamp.from;

import com.inzynierka2k24.apiserver.model.Reservation;
import java.sql.Timestamp;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReservationDao {

  private final JdbcTemplate template;

  private static final String GET_ALL_QUERY = "SELECT * FROM reservations WHERE apartment_id = ?";
  private static final String GET_BY_ID_QUERY =
      "SELECT * FROM reservations WHERE apartment_id = ? AND reservation_id = ?";
  private static final String ADD_QUERY = "INSERT INTO reservations VALUES (default, ?, ?, ?)";
  private static final String DELETE_QUERY =
      "DELETE FROM reservations WHERE apartment_id = ? AND reservation_id = ?";
  private static final String UPDATE_QUERY =
      """
        UPDATE
          reservations
        SET
          start_date = ?,
          end_date = ?
        WHERE
          apartment_id = ? AND reservation_id = ?
        """;

  private static final String PERIOD_FREE_QUERY_FOR_UPDATE =
      """
        SELECT
          COUNT(reservation_id)
        FROM
          reservations
        WHERE
        	apartment_id = ? AND (start_date BETWEEN ? AND ? OR end_date BETWEEN ? AND ?) 
        AND reservation_id != ?
        """;
  private static final String PERIOD_FREE_QUERY_FOR_ADD =
      """
        SELECT
          COUNT(reservation_id)
        FROM
          reservations
        WHERE
        	apartment_id = ? AND (start_date BETWEEN ? AND ? OR end_date BETWEEN ? AND ?)
        """;
  private static final RowMapper<Reservation> reservationRowMapper =
      (rs, rowNum) ->
          new Reservation(
              Optional.of(rs.getLong("reservation_id")),
              rs.getLong("apartment_id"),
              rs.getTimestamp("start_date").toLocalDateTime().toInstant(ZoneOffset.UTC),
              rs.getTimestamp("end_date").toLocalDateTime().toInstant(ZoneOffset.UTC));

  public List<Reservation> getAll(long apartmentId) {
    return template.query(GET_ALL_QUERY, reservationRowMapper, apartmentId);
  }

  public Optional<Reservation> getById(long apartmentId, long reservationId) {
    return Optional.ofNullable(
        DataAccessUtils.singleResult(
            template.query(GET_BY_ID_QUERY, reservationRowMapper, apartmentId, reservationId)));
  }

  public void add(long apartmentId, Reservation reservation) {
    template.update(
        ADD_QUERY, apartmentId, from(reservation.startDate()), from(reservation.endDate()));
  }

  public void update(Reservation reservation) {
    template.update(
        UPDATE_QUERY,
        Timestamp.from(reservation.startDate()),
        Timestamp.from(reservation.endDate()),
        reservation.apartmentId(),
        reservation.id().orElseThrow());
  }

  public void deleteById(long apartmentId, long reservationId) {
    template.update(DELETE_QUERY, apartmentId, reservationId);
  }

  public boolean isTimePeriodFree(Reservation reservation) {
    Timestamp start = from(reservation.startDate());
    Timestamp end = from(reservation.endDate());

    if (reservation.id().isPresent()) {
      return Optional.ofNullable(
              template.queryForObject(
                  PERIOD_FREE_QUERY_FOR_UPDATE,
                  int.class,
                  reservation.apartmentId(),
                  start,
                  end,
                  start,
                  end, reservation.id().get()))
          .orElseThrow()
          == 0;
    }

    return Optional.ofNullable(
                template.queryForObject(
                    PERIOD_FREE_QUERY_FOR_ADD,
                    int.class,
                    reservation.apartmentId(),
                    start,
                    end,
                    start,
                    end))
            .orElseThrow()
        == 0;
  }
}
