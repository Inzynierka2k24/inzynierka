package com.inzynierka2k24.apiserver.grpc.integration;

import static com.inzynierka2k24.apiserver.grpc.integration.RequestBuilder.*;
import static com.inzynierka2k24.apiserver.grpc.util.TimeConverter.toInstant;

import com.inzynierka2k24.*;
import com.inzynierka2k24.apiserver.model.Apartment;
import com.inzynierka2k24.apiserver.model.ExternalAccount;
import com.inzynierka2k24.apiserver.model.ExternalOffer;
import com.inzynierka2k24.apiserver.model.ExternalReservation;
import com.inzynierka2k24.apiserver.model.Reservation;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExternalIntegrationServiceClient {

  private final ExternalIntegrationServiceGrpc.ExternalIntegrationServiceBlockingStub blockingStub;

  public List<ServiceResponse> propagateReservation(
      Reservation reservation,
      Collection<ExternalAccount> accounts,
      Collection<ExternalOffer> offers) {
    var request = buildPropagateReservationRequest(reservation, accounts, offers);
    log.info("Request to External Integration Service: {}", request);

    var response = blockingStub.propagateReservation(request);
    log.info("PropagateReservation response: {}", response);

    return response.getResponseList();
  }

  public List<ExternalReservation> getReservations(
      Instant from,
      Instant to,
      Collection<ExternalAccount> accounts,
      Collection<ExternalOffer> offers,
      long apartmentId) {
    var request = buildGetReservationsRequest(from, to, accounts, offers);
    log.info("Request to External Integration Service: {}", request);

    var response = blockingStub.getReservations(request);
    log.info("GetReservations response: {}", response);

    return response.getReservationList().stream()
        .map(reservation -> convert(reservation, apartmentId))
        .toList();
  }

  public List<ServiceResponse> updateApartmentDetails(
      Apartment apartment, Collection<ExternalAccount> accounts, Collection<ExternalOffer> offers) {
    var request = buildUpdateApartmentDetailsRequest(apartment, accounts, offers);
    log.info("Request to External Integration Service: {}", request);

    var response = blockingStub.updateApartmentDetails(request);
    log.info("UpdateApartmentDetails response: {}", response);

    return response.getResponseList();
  }

  private ExternalReservation convert(
      com.inzynierka2k24.ExternalReservation externalReservation, long apartmentId) {
    var reservations = externalReservation.getReservation();
    return new ExternalReservation(
        new Reservation(
            Optional.empty(),
            apartmentId,
            toInstant(reservations.getStartDate()),
            toInstant(reservations.getEndDate())),
        reservations.hasPrice() ? Optional.of(reservations.getPrice()) : Optional.empty(),
        externalReservation.getService());
  }
}
