package com.inzynierka2k24.messagingservice.service.messaging;

import com.inzynierka2k24.messagingservice.model.Message;

public interface MessageSender {

  void sentMessage(Message message);
}
