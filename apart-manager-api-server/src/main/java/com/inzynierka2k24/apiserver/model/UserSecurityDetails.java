package com.inzynierka2k24.apiserver.model;

import java.util.Collection;
import java.util.List;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@NoArgsConstructor
public class UserSecurityDetails implements UserDetails {

  private String userName;
  private String password;
  private boolean active;
  private List<? extends GrantedAuthority> authorities;

  public UserSecurityDetails(User user) {
    this.userName = user.mail();
    this.password = user.password();
    this.active = user.active();
    this.authorities = user.roles().stream().map(SimpleGrantedAuthority::new).toList();
  }

  @Override
  public String getUsername() {
    return userName;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public boolean isEnabled() {
    return active;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }
}
