package com.inzynierka2k24.apiserver.web.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.inzynierka2k24.Reservation;
import com.inzynierka2k24.apiserver.exception.apartment.ApartmentNotFoundException;
import com.inzynierka2k24.apiserver.exception.reservation.ReservationNotFoundException;
import com.inzynierka2k24.apiserver.service.IntegrationService;
import com.inzynierka2k24.apiserver.web.request.GetReservationsRequest;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class IntegrationControllerTest {

  @Test
  void shouldGetPropagateReservationReturnsResponseEntityWithMap()
      throws ReservationNotFoundException {
    // Given
    long userId = 1;
    long apartmentId = 1;
    long reservationId = 1;
    IntegrationService integrationService = mock(IntegrationService.class);
    Map<String, String> expectedMap = new HashMap<>();
    when(integrationService.propagateReservation(userId, apartmentId, reservationId))
        .thenReturn(expectedMap);
    IntegrationController integrationController = new IntegrationController(integrationService);

    // When
    ResponseEntity<Map<String, String>> responseEntity =
        integrationController.propagateReservation(userId, apartmentId, reservationId);

    // Then
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(responseEntity.getBody()).isEqualTo(expectedMap);
  }

  @Test
  void shouldPostGetReservationsReturnsResponseEntityWithListOfReservations() {
    // Given
    long userId = 1;
    long apartmentId = 1;
    Instant from = Instant.now();
    Instant to = Instant.now().plus(1, ChronoUnit.DAYS);
    com.inzynierka2k24.apiserver.web.request.GetReservationsRequest request =
        new GetReservationsRequest(from, to);
    IntegrationService integrationService = mock(IntegrationService.class);
    when(integrationService.getReservations(userId, apartmentId, from, to)).thenReturn(List.of());
    IntegrationController integrationController = new IntegrationController(integrationService);

    // When
    ResponseEntity<List<Reservation>> responseEntity =
        integrationController.getReservations(userId, apartmentId, request);

    // Then
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(responseEntity.getBody()).isEmpty();
  }

  @Test
  void shouldGetUpdateApartmentDetailsReturnsResponseEntityWithMap()
      throws ApartmentNotFoundException {
    // Given
    long userId = 1;
    long apartmentId = 1;
    IntegrationService integrationService = mock(IntegrationService.class);
    Map<String, String> expectedMap = new HashMap<>();
    when(integrationService.updateApartmentDetails(userId, apartmentId)).thenReturn(expectedMap);
    IntegrationController integrationController = new IntegrationController(integrationService);

    // When
    ResponseEntity<Map<String, String>> responseEntity =
        integrationController.updateApartmentDetails(userId, apartmentId);

    // Then
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(responseEntity.getBody()).isEqualTo(expectedMap);
  }

  @Test
  void shouldGetPropagateReservationThrowsReservationNotFoundException()
      throws ReservationNotFoundException {
    // Given
    long userId = 1;
    long apartmentId = 1;
    long reservationId = 1;
    IntegrationService integrationService = mock(IntegrationService.class);
    when(integrationService.propagateReservation(userId, apartmentId, reservationId))
        .thenThrow(ReservationNotFoundException.class);
    IntegrationController integrationController = new IntegrationController(integrationService);

    // When, Then
    assertThatThrownBy(
            () -> integrationController.propagateReservation(userId, apartmentId, reservationId))
        .isInstanceOf(ReservationNotFoundException.class);
  }

  @Test
  void shouldGetUpdateApartmentDetailsThrowsApartmentNotFoundException()
      throws ApartmentNotFoundException {
    // Given
    long userId = 1;
    long apartmentId = 1;
    IntegrationService integrationService = mock(IntegrationService.class);
    when(integrationService.updateApartmentDetails(userId, apartmentId))
        .thenThrow(ApartmentNotFoundException.class);
    IntegrationController integrationController = new IntegrationController(integrationService);

    // When, Then
    assertThatThrownBy(() -> integrationController.updateApartmentDetails(userId, apartmentId))
        .isInstanceOf(ApartmentNotFoundException.class);
  }
}
