package com.inzynierka2k24.apiserver.dao;

import com.inzynierka2k24.ExternalService;
import com.inzynierka2k24.apiserver.model.ExternalOffer;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ExternalOfferDao {

  private final JdbcTemplate template;

  private static final String GET_ALL_QUERY =
      "SELECT * FROM external_offers WHERE apartment_id = ?";
  private static final String GET_BY_ID_QUERY =
      "SELECT * FROM external_offers WHERE apartment_id = ? AND offer_id = ?";
  private static final String GET_BY_SERVICE_TYPE =
      "SELECT * FROM external_offers WHERE apartment_id = ? AND service_type = ?";
  private static final String ADD_QUERY = "INSERT INTO external_offers VALUES (default, ?, ?, ?)";
  private static final String DELETE_QUERY =
      "DELETE FROM external_offers WHERE apartment_id = ? AND offer_id = ?";
  private static final String UPDATE_QUERY =
      """
        UPDATE
          external_offers
        SET
          service_type = ?,
          external_link = ?
        WHERE
          offer_id = ?
        """;

  private static final RowMapper<ExternalOffer> offerRowMapper =
      (rs, rowNum) ->
          new ExternalOffer(
              Optional.of(rs.getLong("offer_id")),
              ExternalService.forNumber(rs.getInt("service_type")),
              rs.getString("external_link"));

  public List<ExternalOffer> getAll(long apartmentId) {
    return template.query(GET_ALL_QUERY, offerRowMapper, apartmentId);
  }

  public Optional<ExternalOffer> getById(long apartmentId, long offerId) {
    return Optional.ofNullable(
        DataAccessUtils.singleResult(
            template.query(GET_BY_ID_QUERY, offerRowMapper, apartmentId, offerId)));
  }

  public Optional<ExternalOffer> getByServiceType(long apartmentId, ExternalService serviceType) {
    return Optional.ofNullable(
        DataAccessUtils.singleResult(
            template.query(
                GET_BY_SERVICE_TYPE, offerRowMapper, apartmentId, serviceType.getNumber())));
  }

  public void add(long apartmentId, ExternalOffer offer) {
    template.update(ADD_QUERY, apartmentId, offer.serviceType().getNumber(), offer.externalLink());
  }

  public void update(ExternalOffer offer) {
    template.update(
        UPDATE_QUERY,
        offer.serviceType().getNumber(),
        offer.externalLink(),
        offer.id().orElseThrow());
  }

  public void deleteById(long apartmentId, long offerId) {
    template.update(DELETE_QUERY, apartmentId, offerId);
  }
}
