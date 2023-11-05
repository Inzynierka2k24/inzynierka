package com.inzynierka2k24.apiserver.web.dto;

import com.inzynierka2k24.apiserver.model.BillingType;
import com.inzynierka2k24.apiserver.model.MembershipLevel;

public record UserDTO(
        long id,
    String login,
    String emailAddress,
    MembershipLevel level,
    BillingType billingType,
    boolean smsNotifications,
    boolean emailNotifications) {
  public UserDTO(long id, String login, String emailAddress) {
    this(id,login, emailAddress, MembershipLevel.FREE, BillingType.NONE, false, false);
  }
}
