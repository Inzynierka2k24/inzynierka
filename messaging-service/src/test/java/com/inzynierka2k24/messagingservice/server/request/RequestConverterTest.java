package com.inzynierka2k24.messagingservice.server.request;

import static com.inzynierka2k24.messagingservice.server.request.RequestConverter.convertToMessage;
import static org.junit.jupiter.api.Assertions.*;

import com.inzynierka2k24.messagingservice.model.Message;
import org.junit.jupiter.api.Test;

class RequestConverterTest {

  @Test
  void shouldConvertToMessage() {
    // Given
    var expected = new Message("test@example.com", "Test Subject", "Test Content");
    var request =
        com.inzynierka2k24.Message.newBuilder()
            .setReceiver("test@example.com")
            .setSubject("Test Subject")
            .setContent("Test Content")
            .build();

    // When/Then
    assertEquals(expected, convertToMessage(request));
  }
}
