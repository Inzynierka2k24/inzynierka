package com.inzynierka2k24.apiserver.model;

import lombok.Getter;

@Getter
public enum ServiceType {
  UNKNOWN(0),
  AIRBNB(1),
  BOOKING(2);

  private final int number;

  ServiceType(int number) {
    this.number = number;
  }

  public static ServiceType forNumber(int number) {
    return switch (number) {
      case 1 -> AIRBNB;
      case 2 -> BOOKING;
      default -> UNKNOWN;
    };
  }
}
