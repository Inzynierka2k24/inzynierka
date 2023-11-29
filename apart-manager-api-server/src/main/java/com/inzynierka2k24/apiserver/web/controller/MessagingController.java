package com.inzynierka2k24.apiserver.web.controller;

import com.inzynierka2k24.apiserver.exception.apartment.ApartmentNotFoundException;
import com.inzynierka2k24.apiserver.exception.messaging.ContactNotFoundException;
import com.inzynierka2k24.apiserver.service.MessagingService;
import com.inzynierka2k24.apiserver.web.dto.ScheduledMessageDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
public class MessagingController {

  private final MessagingService messagingService;

  @PostMapping("/messaging/{userId}")
  public ResponseEntity<Boolean> addScheduledMessageForUser(
      @PathVariable long userId, @RequestBody ScheduledMessageDTO message) {
    log.info("Got message: " + message.message());
    try {
      messagingService.addMessage(userId, message);
    } catch (ApartmentNotFoundException | ContactNotFoundException e) {
      throw new RuntimeException(e);
    }
    return ResponseEntity.ok(true);
  }
}
