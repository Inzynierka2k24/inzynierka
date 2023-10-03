package com.inzynierka2k24.apiserver.dao;

import com.inzynierka2k24.apiserver.model.Apartment;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApartmentDao {
  private final JdbcTemplate template;

  public List<Apartment> getAll(long userId) {
    return template.query(
        "SELECT * FROM apartments WHERE user_id = ?", this::apartmentRowMapper, userId);
  }

  public Optional<Apartment> getById(long userId, long apartmentId) {
    return Optional.ofNullable(
        DataAccessUtils.singleResult(
            template.query(
                "SELECT * FROM apartments WHERE user_id = ? and apartment_id = ?",
                this::apartmentRowMapper,
                userId,
                apartmentId)));
  }

  public void add(long userId, Apartment apartment) {
    template.update(
        "INSERT INTO apartments VALUES (default, ?, ?, ?, ?, ?, ?, ?, ?)",
        userId,
        apartment.dailyPrice(),
        apartment.title(),
        apartment.country(),
        apartment.city(),
        apartment.street(),
        apartment.buildingNumber(),
        apartment.apartmentNumber());
  }

  public void update(long userId, Apartment apartment) {
    var query =
        """
      UPDATE apartments
      SET daily_price = ?,
          title = ?,
          country = ?,
          city = ?,
          street = ?,
          building_nr = ?,
          apartment_nr = ?
      WHERE user_id = ? and apartment_id = ?
      """;
    template.update(
        query,
        apartment.dailyPrice(),
        apartment.title(),
        apartment.country(),
        apartment.city(),
        apartment.street(),
        apartment.buildingNumber(),
        apartment.apartmentNumber(),
        userId,
        apartment.id().orElseThrow());
  }

  public void deleteById(long userId, long apartmentId) {
    template.update(
        "DELETE FROM apartments WHERE user_id = ? and apartment_id = ?", userId, apartmentId);
  }

  private Apartment apartmentRowMapper(ResultSet rs, int rowNum) throws SQLException {
    return new Apartment(
        Optional.of(rs.getLong("apartment_id")),
        rs.getFloat("daily_price"),
        rs.getString("title"),
        rs.getString("country"),
        rs.getString("city"),
        rs.getString("street"),
        rs.getString("building_nr"),
        rs.getString("apartment_nr"));
  }
}
