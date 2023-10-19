package com.inzynierka2k24.messagingservice.server.request;

import com.inzynierka2k24.messagingservice.model.Message;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MessageConverter {

  public static Message convert(com.inzynierka2k24.Message request) {
    return new Message(request.getReceiver(), request.getContent());
  }
}
