package com.inzynierka2k24.apiserver.grpc.integration;

import static com.inzynierka2k24.apiserver.grpc.integration.RequestBuilder.*;

import com.inzynierka2k24.*;
import com.inzynierka2k24.apiserver.model.Apartment;
import com.inzynierka2k24.apiserver.model.ExternalAccount;
import com.inzynierka2k24.apiserver.model.Reservation;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExternalIntegrationServiceClient {

  private final ExternalIntegrationServiceGrpc.ExternalIntegrationServiceBlockingStub blockingStub;

  public List<ServiceResponse> propagateReservation(
      Reservation reservation, Collection<ExternalAccount> accounts) {
    var request = buildPropagateReservationRequest(reservation, accounts);
    log.info("Request to External Integration Service: {}", request);

    var response = blockingStub.propagateReservation(request);
    log.info("PropagateReservation response: {}", response);

    return response.getResponseList();
  }

  public List<Reservation> getReservations(
      Instant from, Instant to, Collection<ExternalAccount> accounts) {
    var request = buildGetReservationsRequest(from, to, accounts);
    log.info("Request to External Integration Service: {}", request);

    var response = blockingStub.getReservations(request);
    log.info("GetReservations response: {}", response);

    return List.of(); // TODO Return external reservations
  }

  public List<ServiceResponse> updateApartmentDetails(
      Apartment apartment, Collection<ExternalAccount> accounts) {
    var request = buildUpdateApartmentDetailsRequest(apartment, accounts);
    log.info("Request to External Integration Service: {}", request);

    var response = blockingStub.updateApartmentDetails(request);
    log.info("UpdateApartmentDetails response: {}", response);

    return response.getResponseList();
  }
}
