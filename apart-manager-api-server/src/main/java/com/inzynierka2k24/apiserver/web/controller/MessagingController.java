package com.inzynierka2k24.apiserver.web.controller;

import com.inzynierka2k24.apiserver.exception.apartment.ApartmentNotFoundException;
import com.inzynierka2k24.apiserver.exception.messaging.ContactNotFoundException;
import com.inzynierka2k24.apiserver.grpc.messaging.MessagingServiceClient;
import com.inzynierka2k24.apiserver.model.Contact;
import com.inzynierka2k24.apiserver.service.MessagingService;
import com.inzynierka2k24.apiserver.web.dto.ScheduledMessageDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
public class MessagingController {

  private final MessagingService messagingService;
  private final MessagingServiceClient messagingServiceClient;

  @PostMapping("/messaging/{userId}/contact/{contactId}")
  public ResponseEntity<String> addScheduledMessageForContact(
      @PathVariable long userId,
      @PathVariable long contactId,
      @RequestBody ScheduledMessageDTO message)
      throws ApartmentNotFoundException, ContactNotFoundException {
    log.info("Got message: " + message.message());
    messagingService.addMessage(userId, contactId, message);
    Contact contact = messagingService.getContactById(userId, contactId);
    messagingServiceClient.sendMessage(contact, message);
    return ResponseEntity.status(HttpStatus.CREATED).body("Message successfully added");
  }

  @DeleteMapping("/messaging/{userId}/contact/{contactId}/message/{messageId}")
  public ResponseEntity<String> deleteScheduledMessageForContact(
      @PathVariable long userId, @PathVariable long contactId, @PathVariable long messageId) {
    log.info("Got message: " + messageId);
    messagingService.deleteMessage(userId, contactId, messageId);
    return ResponseEntity.status(HttpStatus.OK).body("Message successfully deleted");
  }
}
