package com.inzynierka2k24.messagingservice.server.response;

import com.google.protobuf.Timestamp;
import com.inzynierka2k24.GetMessageStatusResponse;
import com.inzynierka2k24.MessageStatus;
import com.inzynierka2k24.messagingservice.model.MessageDetails;
import java.util.Set;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GetMessageStatusResponseGenerator {

  public static GetMessageStatusResponse getResponse(Set<MessageDetails> details) {
    return GetMessageStatusResponse.newBuilder()
        .addAllMessageStatus(
            details.stream()
                .map(
                    message ->
                        MessageStatus.newBuilder()
                            .setStatus(message.messageStatus())
                            .setMessageTime(
                                Timestamp.newBuilder().setSeconds(message.date().getEpochSecond()))
                            .build())
                .toList())
        .build();
  }
}
