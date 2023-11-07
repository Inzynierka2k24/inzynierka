package com.inzynierka2k24.apiserver.model;

import lombok.Getter;

@Getter
public enum EventType {
  UNKNOWN(0),
  RESERVATION(1),
  RENOVATION(2);

  private final int number;

  EventType(int number) {
    this.number = number;
  }

  public static EventType forNumber(int number) {
    return switch (number) {
      case 1 -> RESERVATION;
      case 2 -> RENOVATION;
      default -> UNKNOWN;
    };
  }
}
