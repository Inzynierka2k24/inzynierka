package com.inzynierka2k24.external.server;

import static com.inzynierka2k24.external.TestUtils.createExternalAccount;
import static org.mockito.Mockito.*;

import com.google.protobuf.Timestamp;
import com.inzynierka2k24.*;
import com.inzynierka2k24.external.TestUtils;
import com.inzynierka2k24.external.server.request.InvalidRequestException;
import com.inzynierka2k24.external.server.request.RequestValidator;
import com.inzynierka2k24.external.server.request.ValidationError;
import com.inzynierka2k24.external.service.IntegrationService;
import io.grpc.stub.StreamObserver;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Test;

class GrpcServerTest {

  private final IntegrationService integrationService = mock(IntegrationService.class);
  private final RequestValidator validator = mock(RequestValidator.class);
  private final GrpcServer grpcServer = new GrpcServer(validator, integrationService);

  @Test
  void shouldPropagateReservation() {
    // Given
    PropagateReservationRequest request =
        PropagateReservationRequest.newBuilder()
            .setReservation(
                Reservation.newBuilder()
                    .setStartDate(Timestamp.newBuilder().setSeconds(1630444800))
                    .setEndDate(Timestamp.newBuilder().setSeconds(1630531200))
                    .build())
            .addAccounts(createExternalAccount(ExternalService.BOOKING))
            .addAccounts(createExternalAccount(ExternalService.AIRBNB))
            .build();

    when(validator.validate(request)).thenReturn(null);

    Set<ServiceResponse> serviceResponses =
        Set.of(
            ServiceResponse.newBuilder()
                .setService(ExternalService.BOOKING)
                .setStatus(ResponseStatus.SUCCESS)
                .build(),
            ServiceResponse.newBuilder()
                .setService(ExternalService.AIRBNB)
                .setStatus(ResponseStatus.SUCCESS)
                .build());
    when(integrationService.propagateReservation(any(), any())).thenReturn(serviceResponses);

    StreamObserver<PropagateReservationResponse> responseObserver = mock(StreamObserver.class);

    // When
    grpcServer.propagateReservation(request, responseObserver);

    // Then
    verify(validator).validate(request);
    verify(integrationService).propagateReservation(any(), any());
    verify(responseObserver).onNext(any(PropagateReservationResponse.class));
    verify(responseObserver).onCompleted();
  }

  @Test
  void shouldGetReservations() {
    // Given
    GetReservationsRequest request =
        GetReservationsRequest.newBuilder()
            .addAccounts(createExternalAccount(ExternalService.BOOKING))
            .addAccounts(createExternalAccount(ExternalService.AIRBNB))
            .build();

    Set<com.inzynierka2k24.external.model.Reservation> reservations =
        Set.of(
            new com.inzynierka2k24.external.model.Reservation(
                Instant.EPOCH,
                Instant.EPOCH.plus(10, ChronoUnit.DAYS),
                Optional.of(100f),
                ExternalService.BOOKING));
    when(integrationService.getReservations(any())).thenReturn(reservations);

    StreamObserver<GetReservationsResponse> responseObserver = mock(StreamObserver.class);

    // When
    grpcServer.getReservations(request, responseObserver);

    // Then
    verify(integrationService).getReservations(any());
    verify(responseObserver).onNext(any(GetReservationsResponse.class));
    verify(responseObserver).onCompleted();
  }

  @Test
  void shouldUpdateApartmentDetails() {
    // Given
    UpdateApartmentDetailsRequest request =
        createUpdateApartmentDetailsRequest(
            "Title", Set.of(ExternalService.BOOKING, ExternalService.AIRBNB));

    when(validator.validate(request)).thenReturn(null);

    Set<ServiceResponse> serviceResponses =
        Set.of(
            ServiceResponse.newBuilder()
                .setService(ExternalService.BOOKING)
                .setStatus(ResponseStatus.SUCCESS)
                .build(),
            ServiceResponse.newBuilder()
                .setService(ExternalService.AIRBNB)
                .setStatus(ResponseStatus.SUCCESS)
                .build());
    when(integrationService.updateApartmentDetails(any(), any())).thenReturn(serviceResponses);

    StreamObserver<UpdateApartmentDetailsResponse> responseObserver = mock(StreamObserver.class);

    // When
    grpcServer.updateApartmentDetails(request, responseObserver);

    // Then
    verify(validator).validate(request);
    verify(integrationService).updateApartmentDetails(any(), any());
    verify(responseObserver).onNext(any(UpdateApartmentDetailsResponse.class));
    verify(responseObserver).onCompleted();
  }

  @Test
  void shouldThrowInvalidRequestExceptionIfValidationErrorWhenPropagatingReservation() {
    // Given
    PropagateReservationRequest request =
        PropagateReservationRequest.newBuilder()
            .setReservation(
                Reservation.newBuilder()
                    .setStartDate(Timestamp.newBuilder().setSeconds(1630444800))
                    .setEndDate(Timestamp.newBuilder().setSeconds(1630531200))
                    .build())
            .addAccounts(createExternalAccount(ExternalService.BOOKING))
            .addAccounts(createExternalAccount(ExternalService.AIRBNB))
            .build();

    ValidationError validationError = ValidationError.INVALID_DATE;
    when(validator.validate(request)).thenReturn(validationError);

    StreamObserver<PropagateReservationResponse> responseObserver = mock(StreamObserver.class);

    // When
    grpcServer.propagateReservation(request, responseObserver);

    // Then
    verify(validator).validate(request);
    verify(responseObserver).onError(any(InvalidRequestException.class));
    verifyNoInteractions(integrationService);
  }

  @Test
  void shouldThrowInvalidRequestExceptionIfValidationErrorWhenUpdatingApartmentDetails() {
    // Given
    UpdateApartmentDetailsRequest request =
        createUpdateApartmentDetailsRequest(
            "", Set.of(ExternalService.BOOKING, ExternalService.AIRBNB));

    ValidationError validationError = ValidationError.EMPTY_STRING;
    when(validator.validate(request)).thenReturn(validationError);

    StreamObserver<UpdateApartmentDetailsResponse> responseObserver = mock(StreamObserver.class);

    // When
    grpcServer.updateApartmentDetails(request, responseObserver);

    // Then
    verify(validator).validate(request);
    verify(responseObserver).onError(any(InvalidRequestException.class));
    verifyNoInteractions(integrationService);
  }

  private UpdateApartmentDetailsRequest createUpdateApartmentDetailsRequest(
      String title, Set<ExternalService> services) {
    return UpdateApartmentDetailsRequest.newBuilder()
        .setDetails(
            ApartmentDetails.newBuilder()
                .setTitle(title)
                .setCity("city")
                .setStreet("street")
                .setBuildingNumber("123")
                .setDescription("description")
                .build())
        .addAllAccounts(services.stream().map(TestUtils::createExternalAccount).toList())
        .build();
  }
}
