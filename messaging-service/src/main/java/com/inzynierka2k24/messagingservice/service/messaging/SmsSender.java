package com.inzynierka2k24.messagingservice.service.messaging;

import com.inzynierka2k24.Status;
import com.inzynierka2k24.messagingservice.model.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SmsSender implements MessageSender {

  private static final String GATE_PROVIDER = "@plus.com";

  private final MailSender mailSender;

  @Override
  public Status sentMessage(Message message) {
    return mailSender.sentMessage(addGateInfo(message));
  }

  private Message addGateInfo(Message message) {
    return message.withReceiver(message.receiver() + GATE_PROVIDER);
  }
}
