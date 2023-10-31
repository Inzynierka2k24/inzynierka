package com.inzynierka2k24.apiserver.model;

public enum CostSource {
  UNKNOWN(0),
  BOOKING(1),
  PROMOTION(2),
  FINE(3),
  TAX(4),
  CLEANING(5),
  REPAIR(6),
  MAINTENANCE(7);

  private final int number;

  CostSource(int number) {
    this.number = number;
  }

  public static CostSource forNumber(int number) {
    return switch (number) {
      case 1 -> BOOKING;
      case 2 -> PROMOTION;
      case 3 -> FINE;
      case 4 -> TAX;
      case 5 -> CLEANING;
      case 6 -> REPAIR;
      case 7 -> MAINTENANCE;
      default -> UNKNOWN;
    };
  }
}
