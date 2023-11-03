package com.inzynierka2k24.external.server.request;

import static com.inzynierka2k24.external.util.TimeConverter.toInstant;

import com.inzynierka2k24.ApartmentDetails;
import com.inzynierka2k24.PropagateReservationRequest;
import com.inzynierka2k24.Reservation;
import com.inzynierka2k24.UpdateApartmentDetailsRequest;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public final class RequestValidator {

  public ValidationError validate(PropagateReservationRequest request) {
    return validateDates(request.getReservation());
  }

  public ValidationError validate(UpdateApartmentDetailsRequest request) {
    return validateStrings(request.getDetails());
  }

  private ValidationError validateDates(Reservation reservation) {
    var start = toInstant(reservation.getStartDate());
    var end = toInstant(reservation.getEndDate());
    var now = Instant.now();

    return start.isAfter(now) && end.isAfter(now) && end.isAfter(start)
        ? null
        : ValidationError.INVALID_DATE;
  }

  private ValidationError validateStrings(ApartmentDetails details) {
    return details.getTitle().isBlank()
            || details.getCity().isBlank()
            || details.getStreet().isBlank()
            || details.getBuildingNumber().isBlank()
            || details.getDescription().isBlank()
        ? ValidationError.EMPTY_STRING
        : null;
  }
}
