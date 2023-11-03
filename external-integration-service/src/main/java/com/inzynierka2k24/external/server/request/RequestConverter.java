package com.inzynierka2k24.external.server.request;

import static com.inzynierka2k24.external.util.TimeConverter.toInstant;

import com.inzynierka2k24.ApartmentDetails;
import com.inzynierka2k24.Reservation;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RequestConverter {

  public static com.inzynierka2k24.external.model.Reservation convert(Reservation request) {
    return new com.inzynierka2k24.external.model.Reservation(
        toInstant(request.getStartDate()), toInstant(request.getEndDate()));
  }

  public static com.inzynierka2k24.external.model.ApartmentDetails convert(
      ApartmentDetails request) {
    return new com.inzynierka2k24.external.model.ApartmentDetails(
        request.getTitle(),
        request.getCity(),
        request.getStreet(),
        request.getBuildingNumber(),
        request.getDescription());
  }
}
