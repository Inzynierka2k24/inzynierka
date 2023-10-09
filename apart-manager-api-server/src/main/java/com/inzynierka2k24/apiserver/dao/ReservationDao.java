package com.inzynierka2k24.apiserver.dao;

import static java.util.Date.from;

import com.inzynierka2k24.apiserver.model.Reservation;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservationDao {
  private final JdbcTemplate template;

  public List<Reservation> getAll(long apartmentId) {
    return template.query(
        "SELECT * FROM reservations WHERE apartment_id = ?",
        this::reservationRowMapper,
        apartmentId);
  }

  public Optional<Reservation> getById(long apartmentId, long reservationId) {
    return Optional.ofNullable(
        DataAccessUtils.singleResult(
            template.query(
                "SELECT * FROM reservations WHERE apartment_id = ? AND reservation_id = ?",
                this::reservationRowMapper,
                apartmentId,
                reservationId)));
  }

  public void add(long apartmentId, Reservation reservation) {
    if (isTimePeriodFree(apartmentId, from(reservation.startDate()), from(reservation.endDate()))) {
      template.update(
          "INSERT INTO reservations VALUES (default, ?, ?, ?)",
          apartmentId,
          from(reservation.startDate()),
          from(reservation.endDate()));
    }
  }

  public void update(long apartmentId, Reservation reservation) {
    var query =
        """
        UPDATE
          reservations
        SET
          start_date = ?,
          end_date = ?
        WHERE
          apartment_id = ? AND reservation_id = ?
        """;

    if (isTimePeriodFree(apartmentId, from(reservation.startDate()), from(reservation.endDate()))) {
      template.update(
          query,
          reservation.startDate(),
          reservation.endDate(),
          apartmentId,
          reservation.id().orElseThrow());
    }
  }

  public void deleteById(long apartmentId, long reservationId) {
    template.update(
        "DELETE FROM reservations WHERE apartment_id = ? AND reservation_id = ?",
        apartmentId,
        reservationId);
  }

  public boolean isTimePeriodFree(long apartmentId, Date start, Date end) {
    var query =
        """
        SELECT
          COUNT(reservation_id)
        FROM
          reservations
        WHERE
        	apartment_id = ? AND start_date BETWEEN ? AND ? OR end_date BETWEEN ? AND ?
        """;

    return Optional.ofNullable(
                template.queryForObject(query, Integer.class, apartmentId, start, end, start, end))
            .orElseThrow()
        < 1;
  }

  private Reservation reservationRowMapper(ResultSet rs, int rowNum) throws SQLException {
    return new Reservation(
        Optional.of(rs.getLong("reservation_id")),
        rs.getTimestamp("start_date").toInstant(),
        rs.getTimestamp("end_date").toInstant());
  }
}
