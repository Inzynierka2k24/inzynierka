package com.inzynierka2k24.external;

import com.inzynierka2k24.ExternalAccount;
import com.inzynierka2k24.ExternalService;
import com.inzynierka2k24.external.model.Account;

public class TestUtils {

  public static ExternalAccount createExternalAccount(ExternalService service) {
    return ExternalAccount.newBuilder()
        .setAccount(com.inzynierka2k24.Account.getDefaultInstance())
        .setService(service)
        .build();
  }

  public static Account createAccount(ExternalService service) {
    return new Account("test", "test", "test", service);
  }
}
