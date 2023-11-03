package com.inzynierka2k24.external.server.request;

import static com.inzynierka2k24.external.util.TimeConverter.toInstant;

import com.inzynierka2k24.external.model.ApartmentDetails;
import com.inzynierka2k24.external.model.Reservation;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RequestConverter {

  public static Reservation convert(com.inzynierka2k24.Reservation request) {
    return new Reservation(toInstant(request.getStartDate()), toInstant(request.getEndDate()));
  }

  public static ApartmentDetails convert(com.inzynierka2k24.ApartmentDetails request) {
    return new ApartmentDetails(
        request.getTitle(),
        request.getCity(),
        request.getStreet(),
        request.getBuildingNumber(),
        request.getDescription());
  }
}
