package com.inzynierka2k24.apiserver.model;

import lombok.Getter;

@Getter
public enum IntervalType {
  HOURS(0),
  DAYS(1),
  WEEKS(2);

  private final int number;

  IntervalType(int number) {
    this.number = number;
  }

  public static IntervalType forNumber(int number) {
    return switch (number) {
      case 1 -> DAYS;
      case 2 -> WEEKS;
      default -> HOURS;
    };
  }
}
