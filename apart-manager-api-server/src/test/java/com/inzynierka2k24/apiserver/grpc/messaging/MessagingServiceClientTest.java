package com.inzynierka2k24.apiserver.grpc.messaging;

import static com.inzynierka2k24.apiserver.grpc.messaging.RequestBuilder.buildSendMessageRequest;
import static org.mockito.Mockito.*;

import com.inzynierka2k24.*;
import com.inzynierka2k24.apiserver.grpc.util.TimeConverter;
import com.inzynierka2k24.apiserver.model.Contact;
import com.inzynierka2k24.apiserver.model.ContactType;
import com.inzynierka2k24.apiserver.model.IntervalType;
import com.inzynierka2k24.apiserver.model.TriggerType;
import com.inzynierka2k24.apiserver.web.dto.ScheduledMessageDTO;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class MessagingServiceClientTest {

  private final MessagingServiceGrpc.MessagingServiceBlockingStub blockingStub =
      mock(MessagingServiceGrpc.MessagingServiceBlockingStub.class);

  private final MessagingServiceClient messagingServiceClient =
      new MessagingServiceClient(blockingStub);

  @Test
  void sendMessage() {
    Contact contact =
        new Contact(
            Optional.of(1L),
            Optional.of(1L),
            ContactType.CLEANING,
            "test",
            "test",
            "test",
            true,
            true,
            1.0f);
    ScheduledMessageDTO messageDTO =
        new ScheduledMessageDTO(
            Optional.of(1L), new ArrayList<>(), "test", IntervalType.DAYS, 2, TriggerType.CHECKIN);
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
    when(blockingStub.sendMessage(buildSendMessageRequest(message, eventData)))
        .thenReturn(SendMessageResponse.newBuilder().setStatus(Status.PENDING).build());
    messagingServiceClient.sendMessage(contact, messageDTO);
    verify(blockingStub, times(1)).sendMessage(buildSendMessageRequest(message, eventData));
  }
}
