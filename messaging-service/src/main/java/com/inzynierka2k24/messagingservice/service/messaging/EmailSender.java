package com.inzynierka2k24.messagingservice.service.messaging;

import com.inzynierka2k24.Status;
import com.inzynierka2k24.messagingservice.model.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailSender implements MessageSender {

  private final JavaMailSender javaMailSender;

  @Override
  public Status sentMessage(Message message) {
    log.info("Message sent to {}. Content: {}", message.receiver(), message.content());
    javaMailSender.send(convertToMail(message));
    return Status.SUCCESS;
  }

  private SimpleMailMessage convertToMail(Message message) {
    SimpleMailMessage mailMessage = new SimpleMailMessage();

    mailMessage.setTo(message.receiver());
    mailMessage.setSubject(message.subject());
    mailMessage.setText(message.content());

    return mailMessage;
  }
}
