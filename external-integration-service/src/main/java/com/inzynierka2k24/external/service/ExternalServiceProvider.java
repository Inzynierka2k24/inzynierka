package com.inzynierka2k24.external.service;

import java.util.Set;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class ExternalServiceProvider {

  private final BookingService bookingService;
  private final AirbnbService airbnbService;
  private final TrivagoService trivagoService;
  private final NocowaniePlService nocowaniePlService;

  Stream<? extends ExternalService> getServices(Set<com.inzynierka2k24.ExternalService> services) {
    return services.stream()
        .map(
            externalService ->
                switch (externalService) {
                  case BOOKING -> bookingService;
                  case AIRBNB -> airbnbService;
                  case TRIVAGO -> trivagoService;
                  case NOCOWANIEPL -> nocowaniePlService;
                  case UNRECOGNIZED -> throw new IllegalArgumentException("Unrecognized service!");
                });
  }
}
