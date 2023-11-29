package com.inzynierka2k24.apiserver.grpc.integration;

import static com.inzynierka2k24.apiserver.grpc.util.TimeConverter.toProtoTimestamp;
import static org.junit.jupiter.api.Assertions.*;

import com.inzynierka2k24.*;
import com.inzynierka2k24.apiserver.model.Apartment;
import com.inzynierka2k24.apiserver.model.ExternalAccount;
import com.inzynierka2k24.apiserver.model.Reservation;
import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class RequestBuilderTest {

  @Test
  void shouldBuildPropagateReservationRequest() {
    // Given
    Reservation reservation =
        new Reservation(Optional.of(1L), 2L, Instant.now(), Instant.now().plusSeconds(3600));
    Collection<ExternalAccount> accounts =
        List.of(
            new ExternalAccount("login1", "password1", 1),
            new ExternalAccount("login2", "password2", 2));

    // When
    PropagateReservationRequest request =
        RequestBuilder.buildPropagateReservationRequest(reservation, accounts);

    // Then
    assertNotNull(request);
    assertEquals(
        toProtoTimestamp(reservation.startDate()), request.getReservation().getStartDate());
    assertEquals(toProtoTimestamp(reservation.endDate()), request.getReservation().getEndDate());
    assertEquals(accounts.size(), request.getAccountsList().size());
  }

  @Test
  void shouldBuildGetReservationsRequest() {
    // Given
    Instant from = Instant.now();
    Instant to = Instant.now().plusSeconds(3600);
    Collection<ExternalAccount> accounts =
        List.of(
            new ExternalAccount("login1", "password1", 1),
            new ExternalAccount("login2", "password2", 2));

    // When
    GetReservationsRequest request = RequestBuilder.buildGetReservationsRequest(from, to, accounts);

    // Then
    assertNotNull(request);
    assertEquals(toProtoTimestamp(from), request.getFrom());
    assertEquals(toProtoTimestamp(to), request.getTo());
    assertEquals(accounts.size(), request.getAccountsList().size());
  }

  @Test
  void shouldBuildUpdateApartmentDetailsRequest() {
    // Given
    Apartment apartment =
        new Apartment(
            Optional.of(1L),
            100.0f,
            "Apartment 1",
            "Country 1",
            "City 1",
            "Street 1",
            "Building 1",
            "Apartment 1");
    Collection<ExternalAccount> accounts =
        List.of(
            new ExternalAccount("login1", "password1", 1),
            new ExternalAccount("login2", "password2", 2));

    // When
    UpdateApartmentDetailsRequest request =
        RequestBuilder.buildUpdateApartmentDetailsRequest(apartment, accounts);

    // Then
    assertNotNull(request);
    assertEquals(apartment.title(), request.getDetails().getTitle());
    assertEquals(apartment.city(), request.getDetails().getCity());
    assertEquals(apartment.street(), request.getDetails().getStreet());
    assertEquals(apartment.buildingNumber(), request.getDetails().getBuildingNumber());
    assertEquals(accounts.size(), request.getAccountsList().size());
  }

  @Test
  void shouldConvertReservationToProto() {
    // Given
    Reservation reservation =
        new Reservation(Optional.of(1L), 2L, Instant.now(), Instant.now().plusSeconds(3600));
    Collection<ExternalAccount> accounts = Collections.emptyList();

    // When
    PropagateReservationRequest request =
        RequestBuilder.buildPropagateReservationRequest(reservation, accounts);

    // Then
    assertNotNull(request);
    assertEquals(
        toProtoTimestamp(reservation.startDate()), request.getReservation().getStartDate());
    assertEquals(toProtoTimestamp(reservation.endDate()), request.getReservation().getEndDate());
    assertTrue(request.getAccountsList().isEmpty());
  }

  @Test
  void shouldConvertExternalAccountToProto() {
    // Given
    ExternalAccount account =
        new ExternalAccount(Optional.of(1L), "login", "password", ExternalService.forNumber(1));

    // When
    com.inzynierka2k24.ExternalAccount protoAccount = RequestBuilder.toProto(account);

    // Then
    assertNotNull(protoAccount);
    assertEquals(account.login(), protoAccount.getAccount().getLogin());
    assertEquals(account.password(), protoAccount.getAccount().getPassword());
    assertEquals(account.serviceType(), protoAccount.getService());
  }

  @Test
  void shouldConvertApartmentToProto() {
    // Given
    Apartment apartment =
        new Apartment(
            Optional.of(1L), 100.0f, "Apartment 1", "Country", "City", "Street", "1", "A");

    // When
    ApartmentDetails protoApartment = RequestBuilder.toProto(apartment);

    // Then
    assertNotNull(protoApartment);
    assertEquals(apartment.title(), protoApartment.getTitle());
    assertEquals(apartment.city(), protoApartment.getCity());
    assertEquals(apartment.street(), protoApartment.getStreet());
    assertEquals(apartment.buildingNumber(), protoApartment.getBuildingNumber());
    assertEquals(
        String.join(
            ", ",
            apartment.title(),
            apartment.country(),
            apartment.city(),
            apartment.street(),
            apartment.apartmentNumber()),
        protoApartment.getDescription());
  }
}
