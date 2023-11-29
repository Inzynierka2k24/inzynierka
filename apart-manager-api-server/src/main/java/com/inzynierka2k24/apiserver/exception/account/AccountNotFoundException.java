package com.inzynierka2k24.apiserver.exception.account;

public class AccountNotFoundException extends Exception {

  public AccountNotFoundException() {
    super("Account not found!");
  }
}
