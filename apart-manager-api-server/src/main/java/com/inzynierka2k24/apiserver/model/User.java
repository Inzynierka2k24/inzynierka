package com.inzynierka2k24.apiserver.model;

import java.util.Optional;
import java.util.Set;

public record User(
    Optional<Long> id, String mail, String password, boolean active, Set<String> roles) {

  public User(String mail, String password) {
    this(Optional.of(0L), mail, password, true, Set.of("USER"));
  }

  public User(Long id, String mail, String password) {
    this(Optional.of(id), mail, password, true, Set.of("USER"));
  }
}
