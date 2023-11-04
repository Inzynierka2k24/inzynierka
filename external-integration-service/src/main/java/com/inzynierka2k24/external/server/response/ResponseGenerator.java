package com.inzynierka2k24.external.server.response;

import static com.inzynierka2k24.external.util.TimeConverter.toProtoTimestamp;

import com.inzynierka2k24.ExternalReservation;
import com.inzynierka2k24.GetReservationsResponse;
import com.inzynierka2k24.external.model.Reservation;
import java.util.Set;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ResponseGenerator {

  public static GetReservationsResponse getResponse(Set<Reservation> reservations) {
    return GetReservationsResponse.newBuilder()
        .addAllReservation(
            reservations.stream().map(ResponseGenerator::convertToExternalReservation).toList())
        .build();
  }

  private static ExternalReservation convertToExternalReservation(Reservation reservation) {
    return ExternalReservation.newBuilder()
        .setReservation(convertToProtoReservation(reservation))
        .setService(reservation.serviceType())
        .build();
  }

  private static com.inzynierka2k24.Reservation convertToProtoReservation(Reservation reservation) {
    var builder = com.inzynierka2k24.Reservation.newBuilder();

    if (reservation.price().isPresent()) {
      builder.setPrice(reservation.price().get());
    }

    return builder
        .setStartDate(toProtoTimestamp(reservation.start()))
        .setEndDate(toProtoTimestamp(reservation.end()))
        .build();
  }
}
