package com.inzynierka2k24.messagingservice.service.messaging;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.inzynierka2k24.Status;
import com.inzynierka2k24.messagingservice.model.MessageContent;
import com.vonage.client.VonageClient;
import com.vonage.client.sms.MessageStatus;
import com.vonage.client.sms.SmsClient;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.SmsSubmissionResponseMessage;
import com.vonage.client.sms.messages.TextMessage;
import java.util.List;
import org.junit.jupiter.api.Test;

public class SmsSenderTest {

  private final VonageClient vonageClient = mock(VonageClient.class);
  private final SmsClient smsClient = mock(SmsClient.class);
  private final SmsSubmissionResponse response = mock(SmsSubmissionResponse.class);
  private final SmsSubmissionResponseMessage responseMessage =
      mock(SmsSubmissionResponseMessage.class);

  @Test
  public void shouldSendMessage() {
    // Given
    when(responseMessage.getStatus()).thenReturn(MessageStatus.OK);
    when(response.getMessages()).thenReturn(List.of(responseMessage));
    when(smsClient.submitMessage(any(TextMessage.class))).thenReturn(response);
    when(vonageClient.getSmsClient()).thenReturn(smsClient);
    SmsSender smsSender = new SmsSender(vonageClient);
    MessageContent message = new MessageContent("1234567890", "Test Subject", "Test Content");

    // When
    Status result = smsSender.sendMessage(message);

    // Then
    assertEquals(Status.SUCCESS, result);
    verify(vonageClient.getSmsClient(), times(1)).submitMessage(any(TextMessage.class));
    verifyNoMoreInteractions(vonageClient.getSmsClient());
  }

  @Test
  public void shouldNotSendMessageWhenMessageStatusNotOK() {
    // Given
    when(responseMessage.getStatus()).thenReturn(MessageStatus.INTERNAL_ERROR);
    when(response.getMessages()).thenReturn(List.of(responseMessage));
    when(smsClient.submitMessage(any(TextMessage.class))).thenReturn(response);
    when(vonageClient.getSmsClient()).thenReturn(smsClient);
    SmsSender smsSender = new SmsSender(vonageClient);
    MessageContent message = new MessageContent("1234567890", "Test Subject", "Test Content");

    // When
    Status result = smsSender.sendMessage(message);

    // Then
    assertEquals(Status.FAILED, result);
    verify(vonageClient.getSmsClient(), times(1)).submitMessage(any(TextMessage.class));
    verifyNoMoreInteractions(vonageClient.getSmsClient());
  }
}
