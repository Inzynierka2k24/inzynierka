package com.inzynierka2k24.external.model;

import com.inzynierka2k24.ExternalService;
import java.time.Instant;
import java.util.Optional;

public record Reservation(
    Instant start, Instant end, Optional<Float> price, ExternalService serviceType) {

  public Reservation(Instant start, Instant end, float price, ExternalService externalService) {
    this(start, end, Optional.of(price), externalService);
  }

  public Reservation(Instant start, Instant end, ExternalService externalService) {
    this(start, end, Optional.empty(), externalService);
  }

  public Reservation(Instant start, Instant end) {
    this(start, end, Optional.empty(), ExternalService.UNRECOGNIZED);
  }
}
