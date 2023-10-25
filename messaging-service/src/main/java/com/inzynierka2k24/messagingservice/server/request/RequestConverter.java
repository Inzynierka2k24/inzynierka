package com.inzynierka2k24.messagingservice.server.request;

import static com.inzynierka2k24.messagingservice.util.ProtoTimestampToInstantConverter.toInstant;

import com.inzynierka2k24.SendMessageRequest;
import com.inzynierka2k24.Status;
import com.inzynierka2k24.messagingservice.model.Message;
import com.inzynierka2k24.messagingservice.model.MessageContent;
import com.inzynierka2k24.messagingservice.model.MessageDetails;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RequestConverter {

  public static Message convertToMessage(SendMessageRequest request) {
    var message = request.getMessage();
    var event = request.getEventData();

    return new Message(
        new MessageContent(message.getReceiver(), message.getSubject(), message.getContent()),
        new MessageDetails(
            event.getEventId(),
            event.getEventType(),
            toInstant(event.getEventTime()),
            Status.PENDING),
        message.getMessageType());
  }
}
