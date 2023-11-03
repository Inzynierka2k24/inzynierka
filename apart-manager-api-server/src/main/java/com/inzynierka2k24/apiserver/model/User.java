package com.inzynierka2k24.apiserver.model;

import java.util.Optional;
import java.util.Set;

public record User(
    Optional<Long> id, String login, String emailAddress, boolean active, Set<String> roles) {

  public User(String emailAddress, String login) {
    this(Optional.empty(), emailAddress, login, true, Set.of("USER"));
  }

  public User(Long id, String emailAddress, String login) {
    this(Optional.of(id), emailAddress, login, true, Set.of("USER"));
  }
}
