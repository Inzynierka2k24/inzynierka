package com.inzynierka2k24.messagingservice.service.messaging;

import com.inzynierka2k24.Status;
import com.inzynierka2k24.messagingservice.model.MessageContent;

public interface MessageSender {

  Status sendMessage(MessageContent message);
}
