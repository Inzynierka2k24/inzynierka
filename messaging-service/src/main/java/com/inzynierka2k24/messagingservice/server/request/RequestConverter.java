package com.inzynierka2k24.messagingservice.server.request;

import com.inzynierka2k24.messagingservice.model.Message;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RequestConverter {

  public static Message convertToMessage(com.inzynierka2k24.Message request) {
    return new Message(request.getReceiver(), request.getSubject(), request.getContent());
  }
}
