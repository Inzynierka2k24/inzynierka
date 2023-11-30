package com.inzynierka2k24.apiserver.grpc.messaging;

import static com.inzynierka2k24.apiserver.grpc.messaging.RequestBuilder.buildSendMessageRequest;

import com.inzynierka2k24.*;
import com.inzynierka2k24.apiserver.model.Contact;
import com.inzynierka2k24.apiserver.web.dto.ScheduledMessageDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessagingServiceClient {

  private final MessagingServiceGrpc.MessagingServiceBlockingStub blockingStub;

  public SendMessageResponse sendMessage(Contact contact, ScheduledMessageDTO messageDTO) {
    var eventData = EventData.newBuilder().setEventId(messageDTO.id().get()).build();
    var message =
        Message.newBuilder()
            .setMessageType(MessageType.MAIL)
            .setContent(messageDTO.message())
            .setReceiver(contact.mail())
            .setSubject("OINK")
            .build();
    var request = buildSendMessageRequest(message, eventData);
    log.info("Request to Messaging Service: {}", request);

    var response = blockingStub.sendMessage(request);
    log.info("SendMessage response: {}", response);

    return response;
  }
}
