package com.inzynierka2k24.messagingservice.service.messaging;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import com.inzynierka2k24.MessageType;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class MessageSenderProviderTest {

  private final MessageSenderProvider provider =
      new MessageSenderProvider(mock(EmailSender.class), mock(SmsSender.class));

  @ParameterizedTest
  @MethodSource
  void getMessageSender(MessageType messageType, Class<? extends MessageSender> expectedClass) {
    assertEquals(expectedClass, provider.getMessageSender(messageType).getClass());
  }

  static Stream<Arguments> getMessageSender() {
    return Stream.of(
        Arguments.of(MessageType.MAIL, EmailSender.class),
        Arguments.of(MessageType.SMS, SmsSender.class));
  }
}
