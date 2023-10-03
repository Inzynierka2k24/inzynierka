package com.inzynierka2k24.apiserver.model;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UserDetails { // implements UserDetails {

  private String userName;
  private String password;
  private boolean active;
  // private List<GrantedAuthority> authorities;

  //  public StoreUserDetails(User user) {
  //    this.userName = user.userName();
  //    this.password = user.password();
  //    this.active = user.active();
  //    this.authorities =
  //        Arrays.stream(user.roles().split(","))
  //            .map(SimpleGrantedAuthority::new)
  //            .collect(Collectors.toList());
  //  }
  //
  //  @Override
  //  public Collection<? extends GrantedAuthority> getAuthorities() {
  //    return authorities;
  //  }
  //
  //  @Override
  //  public String getPassword() {
  //    return password;
  //  }
  //
  //  @Override
  //  public String getUsername() {
  //    return userName;
  //  }
  //
  //  @Override
  //  public boolean isAccountNonExpired() {
  //    return true;
  //  }
  //
  //  @Override
  //  public boolean isAccountNonLocked() {
  //    return true;
  //  }
  //
  //  @Override
  //  public boolean isCredentialsNonExpired() {
  //    return true;
  //  }
  //
  //  @Override
  //  public boolean isEnabled() {
  //    return active;
  //  }
}
