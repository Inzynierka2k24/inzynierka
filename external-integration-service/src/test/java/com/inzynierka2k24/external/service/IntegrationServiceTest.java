package com.inzynierka2k24.external.service;

import static com.inzynierka2k24.ExternalService.AIRBNB;
import static com.inzynierka2k24.ExternalService.BOOKING;
import static com.inzynierka2k24.external.TestUtils.createAccount;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.inzynierka2k24.ResponseStatus;
import com.inzynierka2k24.ServiceResponse;
import com.inzynierka2k24.external.model.Account;
import com.inzynierka2k24.external.model.ApartmentDetails;
import com.inzynierka2k24.external.model.Reservation;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import org.junit.jupiter.api.Test;

class IntegrationServiceTest {

  private final BookingService bookingService = mock(BookingService.class);
  private final AirbnbService airbnbService = mock(AirbnbService.class);
  private final ExternalServiceProvider externalServiceProvider =
      new ExternalServiceProvider(null, bookingService, airbnbService, null);
  private final IntegrationService integrationService =
      new IntegrationService(externalServiceProvider);

  @Test
  void shouldPropagateReservation() {
    // Given
    Reservation reservation =
        new Reservation(
            Instant.now(), Instant.now().plus(1, ChronoUnit.DAYS), Optional.of(100.0f), BOOKING);
    Collection<Account> externalServices = List.of(createAccount(BOOKING), createAccount(AIRBNB));

    when(bookingService.propagateReservation(reservation)).thenReturn(ResponseStatus.SUCCESS);
    when(bookingService.getServiceType()).thenReturn(BOOKING);
    when(airbnbService.propagateReservation(reservation)).thenReturn(ResponseStatus.SUCCESS);
    when(airbnbService.getServiceType()).thenReturn(AIRBNB);

    // When
    Set<ServiceResponse> result =
        integrationService.propagateReservation(reservation, externalServices);

    // Then
    assertNotNull(result);
    assertEquals(2, result.size());
    assertTrue(
        result.stream()
            .allMatch(
                response ->
                    response.getService() != com.inzynierka2k24.ExternalService.UNRECOGNIZED
                        && response.getStatus() == ResponseStatus.SUCCESS));
  }

  @Test
  void shouldGetReservations() {
    // Given
    Reservation reservationBooking =
        new Reservation(
            Instant.parse("2022-01-01T00:00:00Z"),
            Instant.parse("2022-01-02T00:00:00Z"),
            Optional.of(100.0f),
            BOOKING);
    Reservation reservationAirbnb =
        new Reservation(
            Instant.parse("2022-01-03T00:00:00Z"), Instant.parse("2022-01-04T00:00:00Z"), AIRBNB);
    Collection<Account> externalServices = List.of(createAccount(BOOKING), createAccount(AIRBNB));

    when(bookingService.getReservations()).thenReturn(Set.of(reservationBooking));
    when(airbnbService.getReservations()).thenReturn(Set.of(reservationAirbnb));

    // When
    Set<Reservation> result = integrationService.getReservations(externalServices);

    // Then
    assertNotNull(result);
    assertTrue(result.containsAll(Set.of(reservationBooking, reservationAirbnb)));
  }

  @Test
  void shouldUpdateApartmentDetails() {
    // Given
    ApartmentDetails details =
        new ApartmentDetails("Title", "City", "Street", "BuildingNumber", "Description");
    Collection<Account> externalServices = List.of(createAccount(BOOKING), createAccount(AIRBNB));

    when(bookingService.updateApartmentDetails(details)).thenReturn(ResponseStatus.SUCCESS);
    when(bookingService.getServiceType()).thenReturn(BOOKING);
    when(airbnbService.updateApartmentDetails(details)).thenReturn(ResponseStatus.SUCCESS);
    when(airbnbService.getServiceType()).thenReturn(AIRBNB);

    // When
    Set<ServiceResponse> result =
        integrationService.updateApartmentDetails(details, externalServices);

    // Then
    assertNotNull(result);
    assertEquals(2, result.size());
    assertTrue(
        result.stream()
            .allMatch(
                response ->
                    response.getService() != com.inzynierka2k24.ExternalService.UNRECOGNIZED
                        && response.getStatus() == ResponseStatus.SUCCESS));
  }
}
