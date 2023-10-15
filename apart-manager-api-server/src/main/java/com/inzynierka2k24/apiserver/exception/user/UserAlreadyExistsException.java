package com.inzynierka2k24.apiserver.exception.user;

public class UserAlreadyExistsException extends Exception {

  public UserAlreadyExistsException() {
    super("User with the same email already exists!");
  }
}
