package com.inzynierka2k24.apiserver.grpc.messaging;

import com.inzynierka2k24.*;

public final class RequestBuilder {

  public static SendMessageRequest buildSendMessageRequest(Message message, EventData eventData) {
    return SendMessageRequest.newBuilder()
        .setMessage(toProto(message))
        .setEventData(toProto(eventData))
        .build();
  }

  public static GetMessageStatusRequest buildGetMessageStatusRequest(EventData eventData) {
    return GetMessageStatusRequest.newBuilder().setEventData(toProto(eventData)).build();
  }

  public static com.inzynierka2k24.EventData toProto(EventData eventData) {
    return com.inzynierka2k24.EventData.newBuilder()
        .setEventId(eventData.getEventId())
        .setEventTime(eventData.getEventTime())
        .build();
  }

  public static com.inzynierka2k24.Message toProto(Message message) {
    return com.inzynierka2k24.Message.newBuilder()
        .setReceiver(message.getReceiver())
        .setSubject(message.getSubject())
        .setContent(message.getContent())
        .setMessageType(message.getMessageType())
        .build();
  }
}
