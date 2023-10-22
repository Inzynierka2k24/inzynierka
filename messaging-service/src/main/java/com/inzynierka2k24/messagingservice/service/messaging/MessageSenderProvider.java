package com.inzynierka2k24.messagingservice.service.messaging;

import com.inzynierka2k24.MessageType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageSenderProvider {

  private final EmailSender emailSender;
  private final SmsSender smsSender;

  public MessageSender getMessageSender(MessageType messageType) {
    return switch (messageType) {
      case MAIL -> emailSender;
      case SMS -> smsSender;
      case UNRECOGNIZED -> null;
    };
  }
}
