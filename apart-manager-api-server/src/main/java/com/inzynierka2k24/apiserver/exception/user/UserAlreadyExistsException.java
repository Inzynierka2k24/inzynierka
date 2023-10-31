package com.inzynierka2k24.apiserver.exception.user;

public class UserAlreadyExistsException extends RuntimeException {

  public UserAlreadyExistsException() {
    super("User with the same login or email already exists!");
  }
  public UserAlreadyExistsException(Throwable e) {
    super("User with the same login or email already exists!", e);
  }
}
