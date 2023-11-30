package com.inzynierka2k24.apiserver.grpc.integration;

import static com.inzynierka2k24.apiserver.grpc.util.TimeConverter.toProtoTimestamp;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.inzynierka2k24.*;
import com.inzynierka2k24.apiserver.model.Apartment;
import com.inzynierka2k24.apiserver.model.ExternalAccount;
import com.inzynierka2k24.apiserver.model.Reservation;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class ExternalIntegrationServiceClientTest {

  @Test
  public void shouldPropagateReservationValidRequestNonEmptyAccounts() {
    // Given
    ExternalIntegrationServiceGrpc.ExternalIntegrationServiceBlockingStub blockingStub =
        mock(ExternalIntegrationServiceGrpc.ExternalIntegrationServiceBlockingStub.class);
    ExternalIntegrationServiceClient client = new ExternalIntegrationServiceClient(blockingStub);
    Reservation reservation =
        new Reservation(Optional.of(1L), 1L, Instant.now(), Instant.now().plusSeconds(3600));
    List<ExternalAccount> accounts = List.of(new ExternalAccount(1L, "login", "password", 1));
    PropagateReservationRequest expectedRequest =
        PropagateReservationRequest.newBuilder()
            .setReservation(RequestBuilder.toProto(reservation))
            .addAllAccounts(accounts.stream().map(RequestBuilder::toProto).toList())
            .build();
    PropagateReservationResponse response =
        PropagateReservationResponse.newBuilder()
            .addResponse(ServiceResponse.newBuilder().setStatus(ResponseStatus.SUCCESS).build())
            .build();
    when(blockingStub.propagateReservation(expectedRequest)).thenReturn(response);

    // When
    List<ServiceResponse> result = client.propagateReservation(reservation, accounts);

    // Then
    verify(blockingStub).propagateReservation(expectedRequest);
    assertEquals(1, result.size());
    assertEquals(ResponseStatus.SUCCESS, result.get(0).getStatus());
  }

  @Test
  public void shouldGetReservationsValidRequestNonEmptyAccounts() {
    // Given
    ExternalIntegrationServiceGrpc.ExternalIntegrationServiceBlockingStub blockingStub =
        mock(ExternalIntegrationServiceGrpc.ExternalIntegrationServiceBlockingStub.class);
    ExternalIntegrationServiceClient client = new ExternalIntegrationServiceClient(blockingStub);
    Instant from = Instant.now();
    Instant to = Instant.now().plusSeconds(3600);
    List<ExternalAccount> accounts = List.of(new ExternalAccount(1L, "login", "password", 1));
    GetReservationsRequest expectedRequest =
        GetReservationsRequest.newBuilder()
            .setFrom(toProtoTimestamp(from))
            .setTo(toProtoTimestamp(to))
            .addAllAccounts(accounts.stream().map(RequestBuilder::toProto).toList())
            .build();
    GetReservationsResponse response =
        GetReservationsResponse.newBuilder()
            .addReservation(ExternalReservation.getDefaultInstance())
            .build();
    when(blockingStub.getReservations(expectedRequest)).thenReturn(response);

    // When
    List<Reservation> result = client.getReservations(from, to, accounts);

    // Then
    verify(blockingStub).getReservations(expectedRequest);
    assertEquals(0, result.size()); // TODO Change after implementing method
  }

  @Test
  public void shouldUpdateApartmentDetailsValidRequestNonEmptyAccounts() {
    // Given
    ExternalIntegrationServiceGrpc.ExternalIntegrationServiceBlockingStub blockingStub =
        mock(ExternalIntegrationServiceGrpc.ExternalIntegrationServiceBlockingStub.class);
    ExternalIntegrationServiceClient client = new ExternalIntegrationServiceClient(blockingStub);
    Apartment apartment =
        new Apartment(
            Optional.of(1L),
            100.0f,
            "Title",
            "Country",
            "City",
            "Street",
            "Building",
            "Apartment",
            4);
    List<ExternalAccount> accounts = List.of(new ExternalAccount(1L, "login", "password", 1));
    UpdateApartmentDetailsRequest expectedRequest =
        UpdateApartmentDetailsRequest.newBuilder()
            .setDetails(RequestBuilder.toProto(apartment))
            .addAllAccounts(accounts.stream().map(RequestBuilder::toProto).toList())
            .build();
    UpdateApartmentDetailsResponse response =
        UpdateApartmentDetailsResponse.newBuilder()
            .addResponse(ServiceResponse.newBuilder().setStatus(ResponseStatus.SUCCESS).build())
            .build();
    when(blockingStub.updateApartmentDetails(expectedRequest)).thenReturn(response);

    // When
    List<ServiceResponse> result = client.updateApartmentDetails(apartment, accounts);

    // Then
    verify(blockingStub).updateApartmentDetails(expectedRequest);
    assertEquals(1, result.size());
    assertEquals(ResponseStatus.SUCCESS, result.get(0).getStatus());
  }
}
