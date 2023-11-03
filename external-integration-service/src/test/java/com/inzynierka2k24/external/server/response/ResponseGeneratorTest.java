package com.inzynierka2k24.external.server.response;

import static com.inzynierka2k24.external.util.TimeConverter.toProtoTimestamp;
import static org.assertj.core.api.Assertions.*;

import com.inzynierka2k24.ExternalReservation;
import com.inzynierka2k24.ExternalService;
import com.inzynierka2k24.GetReservationsResponse;
import com.inzynierka2k24.external.model.Reservation;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ResponseGeneratorTest {

  @Test
  void shouldGetResponse() {
    // Given
    Reservation reservationBooking =
        new Reservation(
            Instant.parse("2022-01-01T00:00:00Z"),
            Instant.parse("2022-01-02T00:00:00Z"),
            Optional.of(100.0f),
            ExternalService.BOOKING);
    Reservation reservationAirbnb =
        new Reservation(
            Instant.parse("2022-01-03T00:00:00Z"),
            Instant.parse("2022-01-04T00:00:00Z"),
            ExternalService.AIRBNB);
    Set<Reservation> reservations = Set.of(reservationBooking, reservationAirbnb);

    // When
    GetReservationsResponse response = ResponseGenerator.getResponse(reservations);

    // Then
    List<ExternalReservation> externalReservations = response.getReservationList();
    assertThat(externalReservations)
        .hasSize(2)
        .containsAll(
            List.of(
                ExternalReservation.newBuilder()
                    .setService(ExternalService.BOOKING)
                    .setReservation(
                        com.inzynierka2k24.Reservation.newBuilder()
                            .setStartDate(toProtoTimestamp(reservationBooking.start()))
                            .setEndDate(toProtoTimestamp(reservationBooking.end()))
                            .setPrice(100f)
                            .build())
                    .build(),
                ExternalReservation.newBuilder()
                    .setService(ExternalService.AIRBNB)
                    .setReservation(
                        com.inzynierka2k24.Reservation.newBuilder()
                            .setStartDate(toProtoTimestamp(reservationAirbnb.start()))
                            .setEndDate(toProtoTimestamp(reservationAirbnb.end()))
                            .build())
                    .build()));
  }
}
