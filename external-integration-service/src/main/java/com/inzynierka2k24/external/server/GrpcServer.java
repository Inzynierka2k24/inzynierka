package com.inzynierka2k24.external.server;

import com.inzynierka2k24.ExternalIntegrationServiceGrpc;
import com.inzynierka2k24.GetReservationsRequest;
import com.inzynierka2k24.GetReservationsResponse;
import com.inzynierka2k24.PropagateReservationRequest;
import com.inzynierka2k24.PropagateReservationResponse;
import com.inzynierka2k24.UpdateApartmentDetailsRequest;
import com.inzynierka2k24.UpdateApartmentDetailsResponse;
import com.inzynierka2k24.external.server.request.InvalidRequestException;
import com.inzynierka2k24.external.server.request.RequestValidator;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lognet.springboot.grpc.GRpcService;

@GRpcService
@Slf4j
@RequiredArgsConstructor
public class GrpcServer extends ExternalIntegrationServiceGrpc.ExternalIntegrationServiceImplBase {

  private final RequestValidator validator;

  @Override
  public void propagateReservation(
      PropagateReservationRequest request,
      StreamObserver<PropagateReservationResponse> responseObserver) {
    var validationError = validator.validate(request);

    if (validationError != null) {
      log.error("Error for request: {}. Error: {}", request, validationError);
      responseObserver.onError(new InvalidRequestException(validationError));
      return;
    }

    // responseObserver.onNext();
    responseObserver.onCompleted();
  }

  @Override
  public void getReservations(
      GetReservationsRequest request, StreamObserver<GetReservationsResponse> responseObserver) {
    super.getReservations(request, responseObserver);
  }

  @Override
  public void updateApartmentDetails(
      UpdateApartmentDetailsRequest request,
      StreamObserver<UpdateApartmentDetailsResponse> responseObserver) {
    super.updateApartmentDetails(request, responseObserver);
  }
}
