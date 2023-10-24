package com.inzynierka2k24.messagingservice.server.request;

import static com.inzynierka2k24.messagingservice.server.request.RequestConverter.convertToMessage;
import static org.junit.jupiter.api.Assertions.*;

import com.google.protobuf.Timestamp;
import com.inzynierka2k24.*;
import com.inzynierka2k24.messagingservice.model.Message;
import com.inzynierka2k24.messagingservice.model.MessageContent;
import com.inzynierka2k24.messagingservice.model.MessageDetails;
import java.time.Instant;
import org.junit.jupiter.api.Test;

class RequestConverterTest {

  @Test
  void shouldConvertToMessage() {
    // Given
    var content = new MessageContent("test@example.com", "Test Subject", "Test Content");
    var details = new MessageDetails(1L, EventType.RESERVATION, Instant.EPOCH, Status.PENDING);
    var expected = new Message(content, details, MessageType.MAIL);
    var request =
        SendMessageRequest.newBuilder()
            .setMessage(
                com.inzynierka2k24.Message.newBuilder()
                    .setReceiver("test@example.com")
                    .setSubject("Test Subject")
                    .setContent("Test Content")
                    .build())
            .setEventData(
                EventData.newBuilder()
                    .setEventId(1L)
                    .setEventType(EventType.RESERVATION)
                    .setEventTime(Timestamp.getDefaultInstance())
                    .build())
            .build();

    // When/Then
    assertEquals(expected, convertToMessage(request));
  }
}
