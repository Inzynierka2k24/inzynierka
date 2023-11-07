package com.inzynierka2k24.apiserver.exception.reservation;

public class ReservationNotValidException extends Exception {

  public ReservationNotValidException(String cause) {
    super("Reservation not valid! " + cause);
  }
}
