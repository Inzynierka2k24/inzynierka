package com.inzynierka2k24.external.service;

import com.inzynierka2k24.ResponseStatus;
import com.inzynierka2k24.external.model.ApartmentDetails;
import com.inzynierka2k24.external.model.Reservation;
import java.util.Set;
import org.springframework.stereotype.Service;

@Service
class BookingService implements ExternalService {

  @Override
  public ResponseStatus propagateReservation(Reservation reservation) {
    return null;
  }

  @Override
  public Set<Reservation> getReservations() {
    return null;
  }

  @Override
  public ResponseStatus updateApartmentDetails(ApartmentDetails details) {
    return null;
  }
}
