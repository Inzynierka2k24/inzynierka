package com.inzynierka2k24.messagingservice.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.inzynierka2k24.EventType;
import com.inzynierka2k24.MessageType;
import com.inzynierka2k24.Status;
import com.inzynierka2k24.messagingservice.model.Message;
import com.inzynierka2k24.messagingservice.model.MessageContent;
import com.inzynierka2k24.messagingservice.model.MessageDetails;
import com.inzynierka2k24.messagingservice.repository.MessageRepository;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;

class MessageServiceTest {

  private final MessageRepository repository = mock(MessageRepository.class);
  private final MessageService messageService = new MessageService(repository);

  @Test
  void shouldReceiveStatuses() {
    // Given
    var receiver = "test@mail.com";
    long eventId = 1;
    var eventType = EventType.RESERVATION;
    var expected = Set.of();
    when(repository.findByContent_ReceiverAndDetails_EventIdAndDetails_EventType(
            receiver, eventId, eventType))
        .thenReturn(List.of());

    // When
    var actual = messageService.get(receiver, eventId, eventType);

    // Then
    assertEquals(expected, actual);
  }

  @Test
  void shouldSaveStatuses() {
    // Given
    var content = new MessageContent("test@example.com", "Test Subject", "Test Content");
    var details = new MessageDetails(1L, EventType.RESERVATION, Instant.EPOCH, Status.STORED);
    var message = new Message(content, details, MessageType.MAIL);

    // When/Then
    assertDoesNotThrow(() -> messageService.save(message));
  }
}
