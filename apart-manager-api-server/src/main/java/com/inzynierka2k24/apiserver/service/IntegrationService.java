package com.inzynierka2k24.apiserver.service;

import com.inzynierka2k24.ServiceResponse;
import com.inzynierka2k24.apiserver.exception.apartment.ApartmentNotFoundException;
import com.inzynierka2k24.apiserver.exception.reservation.ReservationNotFoundException;
import com.inzynierka2k24.apiserver.exception.reservation.ReservationNotValidException;
import com.inzynierka2k24.apiserver.grpc.integration.ExternalIntegrationServiceClient;
import com.inzynierka2k24.apiserver.model.EventType;
import com.inzynierka2k24.apiserver.model.ExternalReservation;
import com.inzynierka2k24.apiserver.model.Source;
import com.inzynierka2k24.apiserver.web.dto.FinanceDTO;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
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
          boolean shouldSaveToFinances = true;

          try {
            reservationService.add(apartmentId, reservation.reservation());
          } catch (ReservationNotValidException e) {
            log.error(
                "Couldn't save external reservation {}. Cause: {}", reservation, e.getMessage());
            shouldSaveToFinances = false;
          }

          if (shouldSaveToFinances && reservation.price().isPresent()) {
            financeService.add(
                new FinanceDTO(
                    userId,
                    apartmentId,
                    EventType.forNumber(0).name(),
                    Source.forServiceType(reservation.serviceType()).name(),
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
