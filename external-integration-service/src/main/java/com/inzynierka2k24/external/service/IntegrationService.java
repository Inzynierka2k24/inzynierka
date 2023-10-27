package com.inzynierka2k24.external.service;

import com.inzynierka2k24.ExternalService;
import com.inzynierka2k24.external.model.ApartmentDetails;
import com.inzynierka2k24.external.model.Reservation;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IntegrationService {

  private final ExternalServiceProvider serviceProvider;

  public void propagateReservation(Reservation reservation, Set<ExternalService> externalServices) {
    serviceProvider
        .getServices(externalServices)
        .map(externalService -> externalService.propagateReservation(reservation));
  }

  public void getReservations(Set<ExternalService> externalServices) {
    serviceProvider
        .getServices(externalServices)
        .map(com.inzynierka2k24.external.service.ExternalService::getReservations);
  }

  public void updateApartmentDetails(
      ApartmentDetails details, Set<ExternalService> externalServices) {
    serviceProvider
        .getServices(externalServices)
        .map(externalService -> externalService.updateApartmentDetails(details));
  }
}
