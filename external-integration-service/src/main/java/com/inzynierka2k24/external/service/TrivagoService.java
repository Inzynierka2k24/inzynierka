package com.inzynierka2k24.external.service;

import com.inzynierka2k24.ResponseStatus;
import com.inzynierka2k24.external.model.ApartmentDetails;
import com.inzynierka2k24.external.model.Reservation;
import java.time.Instant;
import java.util.Set;
import org.springframework.stereotype.Service;

@Service
class TrivagoService implements ExternalService {

  @Override
  public ResponseStatus propagateReservation(Reservation reservation) {
    return ResponseStatus.FAILED;
  }

  @Override
  public Set<Reservation> getReservations(Instant from, Instant to) {
    return Set.of();
  }

  @Override
  public ResponseStatus updateApartmentDetails(ApartmentDetails details) {
    return ResponseStatus.FAILED;
  }

  @Override
  public com.inzynierka2k24.ExternalService getServiceType() {
    return com.inzynierka2k24.ExternalService.TRIVAGO;
  }
}
