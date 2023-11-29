package com.inzynierka2k24.apiserver.exception.messaging;

public class ContactNotFoundException extends Exception {

  public ContactNotFoundException() {
    super("Contact not found!");
  }
}
