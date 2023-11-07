package com.inzynierka2k24.apiserver.model;

import lombok.Getter;

@Getter
public enum ContactType {
  UNKNOWN(0),
  CLEANING(1),
  MECHANIC(2),
  ELECTRICIAN(3);

  private final int number;

  ContactType(int number) {
    this.number = number;
  }

  public static ContactType forNumber(int number) {
    return switch (number) {
      case 1 -> CLEANING;
      case 2 -> MECHANIC;
      case 3 -> ELECTRICIAN;
      default -> UNKNOWN;
    };
  }
}
