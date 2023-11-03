package com.inzynierka2k24.apiserver.model;

import java.util.Optional;
import java.util.Set;

public record User(
    Optional<Long> id, String login, String emailAddress, boolean active, Set<String> roles) {

  public User(String login, String emailAddress) {
    this(Optional.empty(), login, emailAddress, true, Set.of("USER"));
  }

  public User(Long id, String login,String emailAddress) {
    this(Optional.of(id), login, emailAddress, true, Set.of("USER"));
  }
}
