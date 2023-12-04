package com.inzynierka2k24.apiserver.model;

import lombok.Getter;
import com.inzynierka2k24.ExternalService;
import lombok.Getter;

@Getter
public enum Source {
  UNKNOWN(0),
  BOOKING(100),
  AIRBNB(101),
  TRIVAGO(102),
  NOCOWANIEPL(103),
  PROMOTION(2),
  FINE(3),
  TAX(4),
  CLEANING(5),
  REPAIR(6),
  MAINTENANCE(7);

  private final int number;

  Source(int number) {
    this.number = number;
  }

  public static Source forNumber(int number) {
    return switch (number) {
      case 100 -> BOOKING;
      case 101 -> AIRBNB;
      case 102 -> TRIVAGO;
      case 103 -> NOCOWANIEPL;
      case 2 -> PROMOTION;
      case 3 -> FINE;
      case 4 -> TAX;
      case 5 -> CLEANING;
      case 6 -> REPAIR;
      case 7 -> MAINTENANCE;
      default -> UNKNOWN;
    };
  }

  public static Source forServiceType(ExternalService serviceType) {
    return switch (serviceType) {
      case BOOKING -> BOOKING;
      case AIRBNB -> AIRBNB;
      case TRIVAGO -> TRIVAGO;
      case NOCOWANIEPL -> NOCOWANIEPL;
      default -> UNKNOWN;
    };
  }
}
