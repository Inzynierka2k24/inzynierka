package com.inzynierka2k24.apiserver.model;

import java.time.Instant;
import java.util.Optional;

public record Finance(
    Optional<Long> id,
    long userId,
    long apartmentId,
    long eventId,
    EventType eventType,
    Source source,
    float price,
    Instant date,
    String details) {

  public Finance(
      Optional<Long> id,
      long userId,
      long apartmentId,
      long eventId,
      int eventType,
      int source,
      float price,
      Instant date,
      String details) {
    this(
        id,
        userId,
        apartmentId,
        eventId,
        EventType.forNumber(eventType),
        Source.forNumber(source),
        price,
        date,
        details);
  }
}
