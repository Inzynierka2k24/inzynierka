package com.inzynierka2k24.external.service;

import com.inzynierka2k24.ExternalService;
import com.inzynierka2k24.ServiceResponse;
import com.inzynierka2k24.external.model.ApartmentDetails;
import com.inzynierka2k24.external.model.Reservation;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IntegrationService {

  private final ExternalServiceProvider serviceProvider;

  public Set<ServiceResponse> propagateReservation(
      Reservation reservation, Collection<ExternalService> externalServices) {
    return serviceProvider
        .getServices(externalServices)
        .map(
            externalService ->
                ServiceResponse.newBuilder()
                    .setService(externalService.getServiceType())
                    .setStatus(externalService.propagateReservation(reservation))
                    .build())
        .collect(Collectors.toSet());
  }

  public Set<Reservation> getReservations(Collection<ExternalService> externalServices) {
    return serviceProvider
        .getServices(externalServices)
        .map(com.inzynierka2k24.external.service.ExternalService::getReservations)
        .flatMap(Collection::stream)
        .collect(Collectors.toSet());
  }

  public Set<ServiceResponse> updateApartmentDetails(
      ApartmentDetails details, Collection<ExternalService> externalServices) {
    return serviceProvider
        .getServices(externalServices)
        .map(
            externalService ->
                ServiceResponse.newBuilder()
                    .setService(externalService.getServiceType())
                    .setStatus(externalService.updateApartmentDetails(details))
                    .build())
        .collect(Collectors.toSet());
  }
}
