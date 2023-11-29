package com.inzynierka2k24.apiserver.model;

import java.util.Optional;

public record ScheduledMessage(
    Optional<Long> id,
    long userId,
    long contactId,
    long apartmentId,
    String message,
    IntervalType intervalType,
    int intervalValue,
    TriggerType triggerType) {
  public ScheduledMessage(
      long userId,
      long contactId,
      long apartmentId,
      String message,
      IntervalType intervalType,
      int intervalValue,
      TriggerType triggerType) {
    this(
        Optional.of(0L),
        userId,
        contactId,
        apartmentId,
        message,
        intervalType,
        intervalValue,
        triggerType);
  }
}
