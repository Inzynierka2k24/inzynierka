package com.inzynierka2k24.external;

import static com.inzynierka2k24.ExternalService.*;
import static com.inzynierka2k24.external.util.TimeConverter.toProtoTimestamp;

import com.inzynierka2k24.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SimpleExternalIntegrationServiceClient {
  private static final String API_URL = "localhost";
  private static final int PORT = 6565;

  public static void main(String[] args) {
    ManagedChannel channel =
        ManagedChannelBuilder.forAddress(API_URL, PORT)
            .usePlaintext()
            .keepAliveTime(5, TimeUnit.SECONDS)
            .executor(Executors.newFixedThreadPool(1))
            .build();

    new SimpleExternalIntegrationServiceClient(channel);
  }

  public SimpleExternalIntegrationServiceClient(ManagedChannel channel) {
    var blockingStub = ExternalIntegrationServiceGrpc.newBlockingStub(channel);
    propagateReservation(blockingStub);
    getReservations(blockingStub);
    updateApartmentDetails(blockingStub);
  }

  private void propagateReservation(
      ExternalIntegrationServiceGrpc.ExternalIntegrationServiceBlockingStub blockingStub) {
    var request =
        PropagateReservationRequest.newBuilder()
            .setReservation(
                Reservation.newBuilder()
                    .setStartDate(toProtoTimestamp(Instant.now().plus(2L, ChronoUnit.DAYS)))
                    .setEndDate(toProtoTimestamp(Instant.now().plus(5L, ChronoUnit.DAYS)))
                    .build())
            .addAccounts(createAccount())
            .build();
    log.info(String.valueOf(request));
    var start = Instant.now();
    var response = blockingStub.propagateReservation(request);

    log.info(
        "PropagateReservation response: {} Connection active for {}",
        response,
        Duration.between(start, Instant.now()));
  }

  private void getReservations(
      ExternalIntegrationServiceGrpc.ExternalIntegrationServiceBlockingStub blockingStub) {
    var from = Instant.parse("2023-11-27T00:00:00Z");
    var to = Instant.parse("2023-12-17T00:00:00Z");
    var request =
        GetReservationsRequest.newBuilder()
            .setFrom(toProtoTimestamp(from))
            .setTo(toProtoTimestamp(to))
            .addAccounts(createAccount())
            .build();
    var start = Instant.now();
    var response = blockingStub.getReservations(request);

    log.info(
        "GetReservations response: {} Connection active for {}",
        response,
        Duration.between(start, Instant.now()));
  }

  private void updateApartmentDetails(
      ExternalIntegrationServiceGrpc.ExternalIntegrationServiceBlockingStub blockingStub) {
    var request =
        UpdateApartmentDetailsRequest.newBuilder()
            .setDetails(
                ApartmentDetails.newBuilder()
                    .setTitle("Apartment")
                    .setCity("City")
                    .setStreet("Street")
                    .setBuildingNumber("1A")
                    .setDescription("Description")
                    .build())
            .addAccounts(createAccount())
            .build();
    var start = Instant.now();
    var response = blockingStub.updateApartmentDetails(request);

    log.info(
        "UpdateApartmentDetails response: {} Connection active for {}",
        response,
        Duration.between(start, Instant.now()));
  }

  private ExternalAccount createAccount() {
    return ExternalAccount.newBuilder()
        .setAccount(Account.newBuilder().setLogin("").setPassword("").setLink("").build())
        .setService(NOCOWANIEPL)
        .build();
  }
}
