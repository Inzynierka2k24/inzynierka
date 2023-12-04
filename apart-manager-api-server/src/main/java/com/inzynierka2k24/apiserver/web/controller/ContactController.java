package com.inzynierka2k24.apiserver.web.controller;

import com.inzynierka2k24.apiserver.model.Contact;
import com.inzynierka2k24.apiserver.service.MessagingService;
import com.inzynierka2k24.apiserver.web.dto.ContactDTO;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
public class ContactController {

  private final MessagingService messagingService;

  @GetMapping("/contacts/{userId}")
  public ResponseEntity<List<ContactDTO>> getContactsForUser(@PathVariable long userId) {
    List<ContactDTO> contactDTOs = messagingService.getContacts(userId);
    return ResponseEntity.ok(contactDTOs);
  }

  @PostMapping("/contacts/{userId}")
  public ResponseEntity<String> addContactForUser(
      @PathVariable long userId, @RequestBody Contact contact) {
    log.info("Got contact: " + contact.name());
    messagingService.addContact(userId, contact);
    return ResponseEntity.status(HttpStatus.CREATED).body("Successfully added contact");
  }

  @PutMapping("/contacts/{userId}/edit")
  public ResponseEntity<String> updateContactForUser(
      @PathVariable long userId, @RequestBody Contact contact) {
    log.info("Got contact: " + contact.name());
    messagingService.updateContact(userId, contact);
    return ResponseEntity.ok("Successfully updated contact");
  }

  @DeleteMapping("/contacts/{userId}/{contactId}")
  public ResponseEntity<String> deleteContactForUser(
      @PathVariable long userId, @PathVariable long contactId) {
    log.info("Got contact: " + contactId);
    messagingService.deleteContact(userId, contactId);
    return ResponseEntity.ok("Successfully deleted contact");
  }
}
