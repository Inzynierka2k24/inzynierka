package com.inzynierka2k24.apiserver.model;

import java.util.Optional;

public record User(
    Optional<Long> id, String login, String password, String mail, boolean active, String roles) {

  public User(String login, String password, String mail) {
    this(Optional.of(0L), login, password, mail, true, "USER");
  }

  public User(Long id, String password, String mail) {
    this(Optional.of(id), "", password, mail, true, "USER");
  }
}
