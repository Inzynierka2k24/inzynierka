package com.inzynierka2k24.messagingservice.service;

import static org.junit.jupiter.api.Assertions.*;

import com.inzynierka2k24.EventType;
import com.inzynierka2k24.Status;
import com.inzynierka2k24.messagingservice.model.MessageStatus;
import java.time.Instant;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Test;

class MessageStatusServiceTest {

  private final MessageStatusService messageStatusService = new MessageStatusService();

  @Test
  void shouldReceiveStatuses() {
    // Given
    var receiver = "test@mail.com";
    var expected = Set.of();

    // When
    var actual = messageStatusService.get(receiver);

    // Then
    assertEquals(expected, actual);
  }

  @Test
  void shouldSaveStatuses() {
    // Given
    var status =
        new MessageStatus(
            "test@mail.com", Status.STORED, Optional.of(1L), EventType.RESERVATION, Instant.EPOCH);

    // When/Then
    assertDoesNotThrow(() -> messageStatusService.save(status));
  }
}
