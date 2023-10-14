package com.inzynierka2k24.apiserver.exception.apartment;

public class ApartmentNotFoundException extends Exception {

  public ApartmentNotFoundException() {
    super("Apartment not found!");
  }
}
