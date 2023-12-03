package com.inzynierka2k24.apiserver.grpc.integration;

import static com.inzynierka2k24.apiserver.grpc.util.TimeConverter.toProtoTimestamp;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.inzynierka2k24.*;
import com.inzynierka2k24.apiserver.model.Apartment;
import com.inzynierka2k24.apiserver.model.ExternalAccount;
import com.inzynierka2k24.apiserver.model.ExternalOffer;
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
    List<ExternalOffer> offers = List.of(new ExternalOffer(1L, 1, "Apartment"));
    PropagateReservationRequest expectedRequest =
        PropagateReservationRequest.newBuilder()
            .setReservation(RequestBuilder.toProto(reservation))
            .addAllAccounts(RequestBuilder.buildExternalAccounts(accounts, offers))
            .build();
    PropagateReservationResponse response =
        PropagateReservationResponse.newBuilder()
            .addResponse(ServiceResponse.newBuilder().setStatus(ResponseStatus.SUCCESS).build())
            .build();
    when(blockingStub.propagateReservation(expectedRequest)).thenReturn(response);

    // When
    List<ServiceResponse> result = client.propagateReservation(reservation, accounts, offers);

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
    List<ExternalOffer> offers = List.of(new ExternalOffer(1L, 1, "Apartment"));
    GetReservationsRequest expectedRequest =
        GetReservationsRequest.newBuilder()
            .setFrom(toProtoTimestamp(from))
            .setTo(toProtoTimestamp(to))
            .addAllAccounts(RequestBuilder.buildExternalAccounts(accounts, offers))
            .build();
    GetReservationsResponse response =
        GetReservationsResponse.newBuilder()
            .addReservation(ExternalReservation.getDefaultInstance())
            .build();
    when(blockingStub.getReservations(expectedRequest)).thenReturn(response);

    // When
    List<com.inzynierka2k24.apiserver.model.ExternalReservation> result =
        client.getReservations(from, to, accounts, offers, 1);

    // Then
    verify(blockingStub).getReservations(expectedRequest);
    assertThat(result).hasSize(1);
  }

  @Test
  public void shouldUpdateApartmentDetailsValidRequestNonEmptyAccounts() {
    // Given
    ExternalIntegrationServiceGrpc.ExternalIntegrationServiceBlockingStub blockingStub =
        mock(ExternalIntegrationServiceGrpc.ExternalIntegrationServiceBlockingStub.class);
    ExternalIntegrationServiceClient client = new ExternalIntegrationServiceClient(blockingStub);
    Apartment apartment =
        new Apartment(
            Optional.of(1L), 100.0f, "Title", "Country", "City", "Street", "Building", "Apartment");
    List<ExternalAccount> accounts = List.of(new ExternalAccount(1L, "login", "password", 1));
    List<ExternalOffer> offers = List.of(new ExternalOffer(1L, 1, "Apartment"));
    UpdateApartmentDetailsRequest expectedRequest =
        UpdateApartmentDetailsRequest.newBuilder()
            .setDetails(RequestBuilder.toProto(apartment))
            .addAllAccounts(RequestBuilder.buildExternalAccounts(accounts, offers))
            .build();
    UpdateApartmentDetailsResponse response =
        UpdateApartmentDetailsResponse.newBuilder()
            .addResponse(ServiceResponse.newBuilder().setStatus(ResponseStatus.SUCCESS).build())
            .build();
    when(blockingStub.updateApartmentDetails(expectedRequest)).thenReturn(response);

    // When
    List<ServiceResponse> result = client.updateApartmentDetails(apartment, accounts, offers);

    // Then
    verify(blockingStub).updateApartmentDetails(expectedRequest);
    assertEquals(1, result.size());
    assertEquals(ResponseStatus.SUCCESS, result.get(0).getStatus());
  }
}
