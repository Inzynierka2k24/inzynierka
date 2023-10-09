package com.inzynierka2k24.apiserver.model;

import java.util.List;
import java.util.Optional;

public record User(
    Optional<Long> id, String mail, String password, boolean active, List<String> roles) {

  public User(String mail, String password) {
    this(Optional.of(0L), mail, password, true, List.of("USER"));
  }

  public User(Long id, String mail, String password) {
    this(Optional.of(id), mail, password, true, List.of("USER"));
  }
}
