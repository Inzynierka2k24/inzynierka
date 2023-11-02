package com.inzynierka2k24.external.model;

import com.inzynierka2k24.ExternalService;
import java.time.Instant;
import java.util.Optional;
import lombok.With;

public record Reservation(
    Instant start, Instant end, Optional<Float> price, @With ExternalService serviceType) {

  public Reservation(Instant start, Instant end, float price) {
    this(start, end, Optional.of(price), ExternalService.UNRECOGNIZED);
  }

  public Reservation(Instant start, Instant end) {
    this(start, end, Optional.empty(), ExternalService.UNRECOGNIZED);
  }
}
