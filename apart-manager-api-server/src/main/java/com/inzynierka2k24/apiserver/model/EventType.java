package com.inzynierka2k24.apiserver.model;

import java.util.List;
import lombok.Getter;

@Getter
public enum EventType {
  UNKNOWN(0, List.of(Source.UNKNOWN)),
  RESERVATION(1, List.of(Source.UNKNOWN, Source.BOOKING, Source.AIRBNB, Source.TRIVAGO, Source.NOCOWANIEPL, Source.PROMOTION, Source.FINE, Source.TAX)),
  RENOVATION(2, List.of(Source.UNKNOWN, Source.CLEANING, Source.REPAIR, Source.MAINTENANCE));

  private final int number;
  private final List<Source> validSources;

  EventType(int number, List<Source> validSources) {
    this.number = number;
    this.validSources = validSources;
  }

  public static EventType forNumber(int number) {
    return switch (number) {
      case 1 -> RESERVATION;
      case 2 -> RENOVATION;
      default -> UNKNOWN;
    };
  }
}
