package com.inzynierka2k24.apiserver.service;

import com.inzynierka2k24.Reservation;
import com.inzynierka2k24.ServiceResponse;
import com.inzynierka2k24.apiserver.exception.apartment.ApartmentNotFoundException;
import com.inzynierka2k24.apiserver.exception.reservation.ReservationNotFoundException;
import com.inzynierka2k24.apiserver.grpc.integration.ExternalIntegrationServiceClient;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IntegrationService {

  private final ExternalIntegrationServiceClient client;
  private final ExternalAccountService accountService;
  private final ReservationService reservationService;
  private final ApartmentService apartmentService;

  public Map<String, String> propagateReservation(long userId, long apartmentId, long reservationId)
      throws ReservationNotFoundException {
    return toStringMap(
        client.propagateReservation(
            reservationService.getById(apartmentId, reservationId), accountService.getAll(userId)));
  }

  public List<Reservation> getReservations(long userId, Instant from, Instant to) {
    client.getReservations(from, to, accountService.getAll(userId));
    return List.of();
  }

  public Map<String, String> updateApartmentDetails(long userId, long apartmentId)
      throws ApartmentNotFoundException {
    return toStringMap(
        client.updateApartmentDetails(
            apartmentService.getById(userId, apartmentId), accountService.getAll(userId)));
  }

  private Map<String, String> toStringMap(List<ServiceResponse> responses) {
    return responses.stream()
        .collect(
            Collectors.toMap(
                response -> response.getService().toString(),
                response -> response.getStatus().toString()));
  }
}
