package com.inzynierka2k24.apiserver.service;

import com.inzynierka2k24.ServiceResponse;
import com.inzynierka2k24.apiserver.exception.apartment.ApartmentNotFoundException;
import com.inzynierka2k24.apiserver.exception.reservation.ReservationNotFoundException;
import com.inzynierka2k24.apiserver.exception.reservation.ReservationNotValidException;
import com.inzynierka2k24.apiserver.grpc.integration.ExternalIntegrationServiceClient;
import com.inzynierka2k24.apiserver.model.EventType;
import com.inzynierka2k24.apiserver.model.ExternalReservation;
import com.inzynierka2k24.apiserver.model.Finance;
import com.inzynierka2k24.apiserver.model.Source;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IntegrationService {

  private final ExternalIntegrationServiceClient client;
  private final ExternalAccountService accountService;
  private final ExternalOfferService offerService;
  private final ReservationService reservationService;
  private final ApartmentService apartmentService;
  private final FinanceService financeService;

  public Map<String, String> propagateReservation(long userId, long apartmentId, long reservationId)
      throws ReservationNotFoundException {
    return toStringMap(
        client.propagateReservation(
            reservationService.getById(apartmentId, reservationId),
            accountService.getAll(userId),
            offerService.getAll(apartmentId)));
  }

  public List<ExternalReservation> getReservations(
      long userId, long apartmentId, Instant from, Instant to) {
    var reservations =
        client.getReservations(
            from, to, accountService.getAll(userId), offerService.getAll(apartmentId), apartmentId);

    reservations.forEach(
        reservation -> {
          try {
            reservationService.add(apartmentId, reservation.reservation());
          } catch (ReservationNotValidException e) {
            throw new RuntimeException(e);
          }
          if (reservation.price().isPresent()) {
            financeService.add(
                new Finance(
                    Optional.empty(),
                    userId,
                    apartmentId,
                    0,
                    EventType.RESERVATION,
                    Source.BOOKING,
                    reservation.price().get(),
                    Instant.now(),
                    "Reservation from external service"));
          }
        });

    return reservations;
  }

  public Map<String, String> updateApartmentDetails(long userId, long apartmentId)
      throws ApartmentNotFoundException {
    return toStringMap(
        client.updateApartmentDetails(
            apartmentService.getById(userId, apartmentId),
            accountService.getAll(userId),
            offerService.getAll(apartmentId)));
  }

  private Map<String, String> toStringMap(List<ServiceResponse> responses) {
    return responses.stream()
        .collect(
            Collectors.toMap(
                response -> response.getService().toString(),
                response -> response.getStatus().toString()));
  }
}
