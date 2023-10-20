package com.inzynierka2k24.messagingservice.service.messaging;

import com.inzynierka2k24.Status;
import com.inzynierka2k24.messagingservice.model.Message;
import com.vonage.client.VonageClient;
import com.vonage.client.sms.MessageStatus;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.messages.TextMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class SmsSender implements MessageSender {

  private final VonageClient vonageClient;

  @Override
  public Status sentMessage(Message message) {
    SmsSubmissionResponse response =
        vonageClient.getSmsClient().submitMessage(convertToTextMessage(message));

    if (response.getMessages().get(0).getStatus() == MessageStatus.OK) {
      log.info("Message sent successfully.");
      return Status.SUCCESS;
    } else {
      log.info("Message failed with error: " + response.getMessages().get(0).getErrorText());
      return Status.FAILED;
    }
  }

  private TextMessage convertToTextMessage(Message message) {
    return new TextMessage("ApartMgr", "48" + message.receiver(), message.content());
  }
}
