package com.inzynierka2k24.apiserver.exception.user;

public class UserNotFoundException extends Exception {

  public UserNotFoundException() {
    super("User not found!");
  }
}
