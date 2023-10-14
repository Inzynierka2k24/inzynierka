package com.inzynierka2k24.apiserver.exception.reservation;

public class ReservationNotFoundException extends Exception {

  public ReservationNotFoundException() {
    super("Reservation not found!");
  }
}
