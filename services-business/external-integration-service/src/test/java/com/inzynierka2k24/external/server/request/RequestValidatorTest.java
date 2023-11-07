package com.inzynierka2k24.external.server.request;

import static com.inzynierka2k24.external.util.TimeConverter.toProtoTimestamp;
import static org.junit.jupiter.api.Assertions.*;

import com.inzynierka2k24.*;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class RequestValidatorTest {

  private final RequestValidator validator = new RequestValidator();

  @ParameterizedTest
  @MethodSource
  void shouldValidateReservation(
      PropagateReservationRequest request, ValidationError expectedError) {
    assertEquals(expectedError, validator.validate(request));
  }

  @ParameterizedTest
  @MethodSource
  void shouldValidateApartmentDetails(
      UpdateApartmentDetailsRequest request, ValidationError expectedError) {
    assertEquals(expectedError, validator.validate(request));
  }

  static Stream<Arguments> shouldValidateReservation() {
    Instant now = Instant.now();
    Reservation.Builder reservation =
        Reservation.newBuilder()
            .setStartDate(toProtoTimestamp(now.plus(2, ChronoUnit.DAYS)))
            .setEndDate(toProtoTimestamp(now.plus(3, ChronoUnit.DAYS)));
    PropagateReservationRequest.Builder request =
        PropagateReservationRequest.newBuilder().setReservation(reservation.build());

    return Stream.of(
        Arguments.of(request.build(), null),
        Arguments.of(
            request
                .setReservation(
                    reservation
                        .setStartDate(toProtoTimestamp(now.minus(1, ChronoUnit.DAYS)))
                        .build())
                .build(),
            ValidationError.INVALID_DATE),
        Arguments.of(
            request
                .setReservation(
                    reservation.setEndDate(toProtoTimestamp(now.minus(1, ChronoUnit.DAYS))).build())
                .build(),
            ValidationError.INVALID_DATE),
        Arguments.of(
            request
                .setReservation(
                    reservation.setEndDate(toProtoTimestamp(now.plus(1, ChronoUnit.DAYS))).build())
                .build(),
            ValidationError.INVALID_DATE));
  }

  static Stream<Arguments> shouldValidateApartmentDetails() {
    ApartmentDetails.Builder details =
        ApartmentDetails.newBuilder()
            .setTitle("Apartment")
            .setCity("New York")
            .setStreet("Broadway")
            .setBuildingNumber("123")
            .setDescription("A beautiful apartment");
    UpdateApartmentDetailsRequest.Builder request =
        UpdateApartmentDetailsRequest.newBuilder().setDetails(details.build());

    return Stream.of(
        Arguments.of(request.build(), null),
        Arguments.of(
            request.setDetails(details.setTitle("")).build(), ValidationError.EMPTY_STRING),
        Arguments.of(request.setDetails(details.setCity("")).build(), ValidationError.EMPTY_STRING),
        Arguments.of(
            request.setDetails(details.setStreet("")).build(), ValidationError.EMPTY_STRING),
        Arguments.of(
            request.setDetails(details.setBuildingNumber("")).build(),
            ValidationError.EMPTY_STRING),
        Arguments.of(
            request.setDetails(details.setDescription("")).build(), ValidationError.EMPTY_STRING));
  }
}
