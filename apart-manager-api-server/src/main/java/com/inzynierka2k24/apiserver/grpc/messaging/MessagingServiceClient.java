package com.inzynierka2k24.apiserver.grpc.messaging;

import static com.inzynierka2k24.apiserver.grpc.messaging.RequestBuilder.buildSendMessageRequest;

import com.inzynierka2k24.*;
import com.inzynierka2k24.apiserver.grpc.util.TimeConverter;
import com.inzynierka2k24.apiserver.model.Contact;
import com.inzynierka2k24.apiserver.web.dto.ScheduledMessageDTO;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessagingServiceClient {

  private final MessagingServiceGrpc.MessagingServiceBlockingStub blockingStub;

  public void sendMessage(Contact contact, ScheduledMessageDTO messageDTO) {
    var eventData =
        EventData.newBuilder()
            .setEventId(0)
            .setEventTypeValue(messageDTO.triggerType().ordinal())
            .setEventTime(TimeConverter.toProtoTimestamp(Instant.now().plus(3, ChronoUnit.DAYS)))
            .build();
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
  }
}
