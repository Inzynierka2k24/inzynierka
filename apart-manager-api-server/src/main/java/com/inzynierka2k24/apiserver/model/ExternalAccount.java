package com.inzynierka2k24.apiserver.model;

import com.inzynierka2k24.ExternalService;
import java.util.Optional;

public record ExternalAccount(
    Optional<Long> id, String login, String password, ExternalService serviceType) {

  public ExternalAccount(String login, String password, int serviceType) {
    this(Optional.empty(), login, password, ExternalService.forNumber(serviceType));
  }

  public ExternalAccount(long id, String login, String password, int serviceType) {
    this(Optional.of(id), login, password, ExternalService.forNumber(serviceType));
  }
}
