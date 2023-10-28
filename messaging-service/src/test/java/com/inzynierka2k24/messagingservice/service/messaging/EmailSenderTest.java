package com.inzynierka2k24.messagingservice.service.messaging;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.inzynierka2k24.Status;
import com.inzynierka2k24.messagingservice.model.MessageContent;
import org.junit.jupiter.api.Test;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

public class EmailSenderTest {

  private final JavaMailSender mockJavaMailSender = mock(JavaMailSender.class);

  @Test
  public void shouldSendEmail() {
    // Given
    EmailSender emailSender = new EmailSender(mockJavaMailSender);
    MessageContent message = new MessageContent("test@example.com", "Test Subject", "Test Content");

    // When
    Status result = emailSender.sendMessage(message);

    // Then
    assertEquals(Status.SUCCESS, result);
    verify(mockJavaMailSender, times(1)).send(any(SimpleMailMessage.class));
  }

  @Test
  public void shouldNotSendEmailWhenJavaMailSenderThrowsMailException() {
    // Given
    EmailSender emailSender = new EmailSender(mockJavaMailSender);
    MessageContent message = new MessageContent("test@example.com", "Test Subject", "Test Content");
    doThrow(new MailSendException("MailException"))
        .when(mockJavaMailSender)
        .send(any(SimpleMailMessage.class));

    // When
    Status result = emailSender.sendMessage(message);

    // Then
    assertEquals(Status.FAILED, result);
    verify(mockJavaMailSender, times(1)).send(any(SimpleMailMessage.class));
  }
}
