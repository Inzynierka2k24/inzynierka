package com.inzynierka2k24.apiserver.model;

import java.time.Instant;
import java.util.Optional;

public record Finance(
    Optional<Long> id,
    long userId,
    long apartmentId,
    EventType eventType,
    Source source,
    float price,
    Instant date,
    String details) {

  public Finance(
      Optional<Long> id,
      long userId,
      long apartmentId,
      int eventType,
      int source,
      float price,
      Instant date,
      String details) {
    this(
        id,
        userId,
        apartmentId,
        EventType.forNumber(eventType),
        Source.forNumber(source),
        price,
        date,
        details);
  }

  public Finance(
      Optional<Long> id,
      long userId,
      long apartmentId,
      String eventType,
      String source,
      float price,
      Instant date,
      String details) {
    this(
        id,
        userId,
        apartmentId,
        EventType.valueOf(eventType.toUpperCase()),
        Source.valueOf(source.toUpperCase()),
        price,
        date,
        details);
  }
}
