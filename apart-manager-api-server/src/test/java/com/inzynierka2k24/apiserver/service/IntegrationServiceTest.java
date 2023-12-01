package com.inzynierka2k24.apiserver.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.inzynierka2k24.ExternalService;
import com.inzynierka2k24.ResponseStatus;
import com.inzynierka2k24.ServiceResponse;
import com.inzynierka2k24.apiserver.exception.apartment.ApartmentNotFoundException;
import com.inzynierka2k24.apiserver.exception.reservation.ReservationNotFoundException;
import com.inzynierka2k24.apiserver.grpc.integration.ExternalIntegrationServiceClient;
import com.inzynierka2k24.apiserver.model.Apartment;
import com.inzynierka2k24.apiserver.model.ExternalAccount;
import com.inzynierka2k24.apiserver.model.ExternalOffer;
import com.inzynierka2k24.apiserver.model.Reservation;
import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class IntegrationServiceTest {

  private final ExternalIntegrationServiceClient client =
      mock(ExternalIntegrationServiceClient.class);
  private final ExternalAccountService accountService = mock(ExternalAccountService.class);
  private final ExternalOfferService offerService = mock(ExternalOfferService.class);
  private final ReservationService reservationService = mock(ReservationService.class);
  private final ApartmentService apartmentService = mock(ApartmentService.class);
  private final IntegrationService integrationService =
      new IntegrationService(
          client, accountService, offerService, reservationService, apartmentService);

  @Test
  void shouldPropagateReservationValidIdsReturnsMap() throws ReservationNotFoundException {
    // Given
    long userId = 1;
    long apartmentId = 1;
    long reservationId = 1;

    Reservation reservation =
        new Reservation(Optional.of(1L), 1L, Instant.now(), Instant.now().plusSeconds(3600));
    List<ExternalAccount> accounts = List.of(new ExternalAccount(1L, "login", "password", 1));
    List<ExternalOffer> offers = List.of(new ExternalOffer(1L, 1, "Apartment"));
    List<ServiceResponse> responses =
        List.of(
            ServiceResponse.newBuilder()
                .setService(ExternalService.BOOKING)
                .setStatus(ResponseStatus.SUCCESS)
                .build());

    when(reservationService.getById(apartmentId, reservationId)).thenReturn(reservation);
    when(accountService.getAll(userId)).thenReturn(accounts);
    when(offerService.getAll(apartmentId)).thenReturn(offers);
    when(client.propagateReservation(reservation, accounts, offers)).thenReturn(responses);

    // When
    Map<String, String> result =
        integrationService.propagateReservation(userId, apartmentId, reservationId);

    // Then
    assertThat(result).isNotNull().hasSize(1);
  }

  @Test
  void shouldGetReservationsValidUserAndTimeRangeReturnsList() {
    // Given
    long userId = 1;
    long apartmentId = 1;
    Instant from = Instant.now();
    Instant to = Instant.now().plus(Duration.ofDays(7));
    List<ExternalAccount> accounts = List.of(new ExternalAccount(1L, "login", "password", 1));
    List<ExternalOffer> offers = List.of(new ExternalOffer(1L, 1, "Apartment"));

    when(accountService.getAll(userId)).thenReturn(accounts);
    when(client.getReservations(from, to, accounts, offers))
        .thenReturn(
            List.of(
                new Reservation(
                    Optional.of(1L), 1L, Instant.now(), Instant.now().plusSeconds(3600))));

    // When
    List<com.inzynierka2k24.Reservation> result =
        integrationService.getReservations(userId, apartmentId, from, to);

    // Then
    assertThat(result).isNotNull().isEmpty(); // TODO Change after implementing this method
  }

  @Test
  void shouldUpdateApartmentDetailsValidUserAndApartmentIdsReturnsList()
      throws ApartmentNotFoundException {
    // Given
    long userId = 1;
    long apartmentId = 1;

    Apartment apartment =
        new Apartment(
            Optional.of(1L), 100.0f, "Title", "Country", "City", "Street", "Building", "Apartment");
    List<ExternalAccount> accounts = List.of(new ExternalAccount(1L, "login", "password", 1));
    List<ExternalOffer> offers = List.of(new ExternalOffer(1L, 1, "Apartment"));
    List<ServiceResponse> responses =
        List.of(
            ServiceResponse.newBuilder()
                .setService(ExternalService.BOOKING)
                .setStatus(ResponseStatus.SUCCESS)
                .build());

    when(apartmentService.getById(userId, apartmentId)).thenReturn(apartment);
    when(accountService.getAll(userId)).thenReturn(accounts);
    when(offerService.getAll(apartmentId)).thenReturn(offers);
    when(client.updateApartmentDetails(apartment, accounts, offers)).thenReturn(responses);

    // When
    Map<String, String> result = integrationService.updateApartmentDetails(userId, apartmentId);

    // Then
    assertThat(result).isNotNull().hasSize(1);
  }

  @Test
  void shouldPropagateReservationInvalidReservationIdThrowsException()
      throws ReservationNotFoundException {
    // Given
    long userId = 1;
    long apartmentId = 1;
    long reservationId = 1;

    when(reservationService.getById(apartmentId, reservationId))
        .thenThrow(ReservationNotFoundException.class);

    // When/Then
    assertThatThrownBy(
            () -> integrationService.propagateReservation(userId, apartmentId, reservationId))
        .isInstanceOf(ReservationNotFoundException.class);
  }

  @Test
  void shouldGetReservationsInvalidUserIdReturnsEmptyList() {
    // Given
    long userId = 1;
    long apartmentId = 1;
    Instant from = Instant.now();
    Instant to = Instant.now().plus(Duration.ofDays(7));

    when(accountService.getAll(userId)).thenReturn(Collections.emptyList());

    // When
    List<com.inzynierka2k24.Reservation> result =
        integrationService.getReservations(userId, apartmentId, from, to);

    // Then
    assertThat(result).isNotNull().isEmpty();
  }

  @Test
  void shouldUpdateApartmentDetailsInvalidApartmentIdThrowsException()
      throws ApartmentNotFoundException {
    // Given
    long userId = 1;
    long apartmentId = 1;

    when(apartmentService.getById(userId, apartmentId)).thenThrow(ApartmentNotFoundException.class);

    // When/Then
    assertThrows(
        ApartmentNotFoundException.class,
        () -> integrationService.updateApartmentDetails(userId, apartmentId));
  }

  @Test
  void shouldPropagateReservationNoAssociatedAccountsReturnsEmptyMap()
      throws ReservationNotFoundException {
    // Given
    long userId = 1;
    long apartmentId = 1;
    long reservationId = 1;

    Reservation reservation =
        new Reservation(Optional.of(1L), 1L, Instant.now(), Instant.now().plusSeconds(3600));

    when(reservationService.getById(apartmentId, reservationId)).thenReturn(reservation);
    when(accountService.getAll(userId)).thenReturn(Collections.emptyList());

    // When
    Map<String, String> result =
        integrationService.propagateReservation(userId, apartmentId, reservationId);

    // Then
    assertThat(result).isNotNull().isEmpty();
  }
}
