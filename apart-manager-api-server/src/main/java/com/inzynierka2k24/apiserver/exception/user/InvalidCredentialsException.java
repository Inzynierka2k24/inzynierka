package com.inzynierka2k24.apiserver.exception.user;

public class InvalidCredentialsException extends RuntimeException {

  public InvalidCredentialsException(Throwable e) {
    super("Invalid credentials provided", e);
  }
}
