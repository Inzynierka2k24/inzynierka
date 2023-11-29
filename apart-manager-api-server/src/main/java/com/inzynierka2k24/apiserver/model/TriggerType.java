package com.inzynierka2k24.apiserver.model;

import lombok.Getter;

@Getter
public enum TriggerType {
  RESERVATION(0),
  CHECKIN(1),
  CHECKOUT(2);

  private final int number;

  TriggerType(int number) {
    this.number = number;
  }

  public static TriggerType forNumber(int number) {
    return switch (number) {
      case 1 -> CHECKIN;
      case 2 -> CHECKOUT;
      default -> RESERVATION;
    };
  }
}
