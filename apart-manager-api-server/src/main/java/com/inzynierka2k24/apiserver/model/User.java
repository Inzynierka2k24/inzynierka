package com.inzynierka2k24.apiserver.model;

public record User(
    long id, String login, String password, String mail, boolean active, String roles) {

  public User(String login, String password, String mail) {
    this(0, login, password, mail, true, "USER");
  }

  public User(Long id, String password, String mail) {
    this(id, "", password, mail, true, "USER");
  }
}
