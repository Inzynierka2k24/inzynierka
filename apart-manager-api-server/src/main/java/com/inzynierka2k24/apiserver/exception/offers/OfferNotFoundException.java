package com.inzynierka2k24.apiserver.exception.offers;

public class OfferNotFoundException extends Exception {

  public OfferNotFoundException() {
    super("Offer not found!");
  }
}
