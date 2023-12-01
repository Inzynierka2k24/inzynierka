package com.inzynierka2k24.apiserver.exception.offers;

public class OfferNotValidException extends Exception {

  public OfferNotValidException(String cause) {
    super("Offer not valid! " + cause);
  }
}
