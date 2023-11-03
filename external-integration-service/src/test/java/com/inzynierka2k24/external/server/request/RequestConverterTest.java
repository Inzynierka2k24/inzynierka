package com.inzynierka2k24.external.server.request;

import static com.inzynierka2k24.external.util.TimeConverter.toProtoTimestamp;
import static org.junit.jupiter.api.Assertions.*;

import com.inzynierka2k24.external.model.ApartmentDetails;
import com.inzynierka2k24.external.model.Reservation;
import java.time.Instant;
import org.junit.jupiter.api.Test;

class RequestConverterTest {

  @Test
  void shouldConvertToInternalReservation() {
    // Given
    var start = Instant.parse("2022-01-01T00:00:00Z");
    var end = Instant.parse("2022-01-02T00:00:00Z");
    var reservation =
        com.inzynierka2k24.Reservation.newBuilder()
            .setStartDate(toProtoTimestamp(start))
            .setEndDate(toProtoTimestamp(end))
            .build();
    var expected = new Reservation(start, end);

    // When
    var actual = RequestConverter.convert(reservation);

    // Then
    assertEquals(Instant.parse("2022-01-01T00:00:00Z"), actual.start());
    assertEquals(Instant.parse("2022-01-02T00:00:00Z"), actual.end());
    assertEquals(expected, actual);
  }

  @Test
  void shouldConvertToInternalApartmentDetails() {
    // Given
    var apartmentDetails =
        com.inzynierka2k24.ApartmentDetails.newBuilder()
            .setTitle("Title")
            .setCity("City")
            .setStreet("Street")
            .setBuildingNumber("1A")
            .setDescription("Description")
            .build();
    var expected = new ApartmentDetails("Title", "City", "Street", "1A", "Description");

    // When
    var actual = RequestConverter.convert(apartmentDetails);

    // Then
    assertEquals(expected, actual);
  }
}
