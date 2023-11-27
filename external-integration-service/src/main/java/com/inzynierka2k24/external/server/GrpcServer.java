package com.inzynierka2k24.external.server;

import static com.inzynierka2k24.external.server.request.RequestConverter.convert;
import static com.inzynierka2k24.external.server.response.ResponseGenerator.getResponse;

import com.inzynierka2k24.ExternalIntegrationServiceGrpc;
import com.inzynierka2k24.GetReservationsRequest;
import com.inzynierka2k24.GetReservationsResponse;
import com.inzynierka2k24.PropagateReservationRequest;
import com.inzynierka2k24.PropagateReservationResponse;
import com.inzynierka2k24.UpdateApartmentDetailsRequest;
import com.inzynierka2k24.UpdateApartmentDetailsResponse;
import com.inzynierka2k24.external.server.request.InvalidRequestException;
import com.inzynierka2k24.external.server.request.RequestValidator;
import com.inzynierka2k24.external.service.IntegrationService;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lognet.springboot.grpc.GRpcService;

@GRpcService
@Slf4j
@RequiredArgsConstructor
public class GrpcServer extends ExternalIntegrationServiceGrpc.ExternalIntegrationServiceImplBase {

  private final RequestValidator validator;
  private final IntegrationService integrationService;

  @Override
  public void propagateReservation(
      PropagateReservationRequest request,
      StreamObserver<PropagateReservationResponse> responseObserver) {
    var validationError = validator.validate(request);

    if (validationError != null) {
      log.error("Error for request: {} Error: {}", request, validationError);
      responseObserver.onError(new InvalidRequestException(validationError));
      return;
    }

    responseObserver.onNext(
        PropagateReservationResponse.newBuilder()
            .addAllResponse(
                integrationService.propagateReservation(
                    convert(request.getReservation()), convert(request.getAccountsList())))
            .build());
    responseObserver.onCompleted();
  }

  @Override
  public void getReservations(
      GetReservationsRequest request, StreamObserver<GetReservationsResponse> responseObserver) {
    responseObserver.onNext(
        getResponse(integrationService.getReservations(convert(request.getAccountsList()))));
    responseObserver.onCompleted();
  }

  @Override
  public void updateApartmentDetails(
      UpdateApartmentDetailsRequest request,
      StreamObserver<UpdateApartmentDetailsResponse> responseObserver) {
    var validationError = validator.validate(request);

    if (validationError != null) {
      log.error("Error for request: {} Error: {}", request, validationError);
      responseObserver.onError(new InvalidRequestException(validationError));
      return;
    }

    responseObserver.onNext(
        UpdateApartmentDetailsResponse.newBuilder()
            .addAllResponse(
                integrationService.updateApartmentDetails(
                    convert(request.getDetails()), convert(request.getAccountsList())))
            .build());
    responseObserver.onCompleted();
  }
}
