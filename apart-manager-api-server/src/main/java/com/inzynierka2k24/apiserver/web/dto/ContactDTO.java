package com.inzynierka2k24.apiserver.web.dto;

import com.inzynierka2k24.apiserver.model.ContactType;
import com.inzynierka2k24.apiserver.model.NotificationSettings;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public record ContactDTO(
    Optional<Long> id,
    ContactType contactType,
    String name,
    float price,
    String mail,
    String phone,
    NotificationSettings notificationSettings,
    List<ScheduledMessageDTO> messages) {

  public ContactDTO(
      long contactId,
      ContactType contactType,
      String name,
      float price,
      String mail,
      String phone) {
    this(
        Optional.of(contactId),
        contactType,
        name,
        price,
        mail,
        phone,
        new NotificationSettings(false, false),
        new ArrayList<>());
  }
}
