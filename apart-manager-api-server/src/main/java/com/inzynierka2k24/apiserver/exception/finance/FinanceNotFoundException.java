package com.inzynierka2k24.apiserver.exception.finance;

public class FinanceNotFoundException extends Exception {
  public FinanceNotFoundException() {
    super("Finance record not found!");
  }
}
