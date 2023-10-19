package com.inzynierka2k24.messagingservice.service.messaging;

import com.inzynierka2k24.Status;
import com.inzynierka2k24.messagingservice.model.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailSender implements MessageSender {

  private final JavaMailSender javaMailSender;

  @Override
  public Status sentMessage(Message message) {
    javaMailSender.send(convertToMail(message));
    return Status.SUCCESS;
  }

  private SimpleMailMessage convertToMail(Message message) {
    SimpleMailMessage mailMessage = new SimpleMailMessage();

    mailMessage.setTo(message.receiver());
    mailMessage.setSubject(""); // TODO Add subjects
    mailMessage.setText(message.content());

    return mailMessage;
  }
}
