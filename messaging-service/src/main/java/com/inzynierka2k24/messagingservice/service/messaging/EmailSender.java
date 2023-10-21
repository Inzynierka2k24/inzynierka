package com.inzynierka2k24.messagingservice.service.messaging;

import com.inzynierka2k24.Status;
import com.inzynierka2k24.messagingservice.model.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
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
    try {
      javaMailSender.send(convertToMail(message));
      log.info("Message sent to {}.", message.receiver());
      return Status.SUCCESS;
    } catch (MailException e) {
      log.error(
          "Failed sending message to {}. Error message: {}", message.receiver(), e.getMessage());
      return Status.FAILED;
    }
  }

  private SimpleMailMessage convertToMail(Message message) {
    SimpleMailMessage mailMessage = new SimpleMailMessage();
    mailMessage.setTo(message.receiver());
    mailMessage.setSubject(message.subject());
    mailMessage.setText(message.content());

    return mailMessage;
  }
}
