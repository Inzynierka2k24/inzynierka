package com.inzynierka2k24.apiserver.web.controller;

import com.inzynierka2k24.apiserver.exception.user.UserNotFoundException;
import com.inzynierka2k24.apiserver.model.Contact;
import com.inzynierka2k24.apiserver.service.MessagingService;
import com.inzynierka2k24.apiserver.web.dto.ContactDTO;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
public class ContactController {

  private final MessagingService messagingService;

  @GetMapping("/contacts/{userId}")
  public ResponseEntity<List<ContactDTO>> getContactsForUser(@PathVariable long userId) {
    try {
      List<ContactDTO> contactDTOs = messagingService.getContacts(userId);
      return ResponseEntity.ok(contactDTOs);
    } catch (UserNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  @PostMapping("/contacts/{userId}")
  public ResponseEntity<Boolean> addContactForUser(
      @PathVariable long userId, @RequestBody Contact contact) {
    log.info("Got contact: " + contact.name());
    messagingService.addContact(userId, contact);
    return ResponseEntity.ok(true);
  }
}
