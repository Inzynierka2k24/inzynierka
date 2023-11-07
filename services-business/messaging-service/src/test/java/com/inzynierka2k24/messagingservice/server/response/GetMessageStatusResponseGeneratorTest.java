package com.inzynierka2k24.messagingservice.server.response;

import static org.junit.jupiter.api.Assertions.*;

import com.google.protobuf.Timestamp;
import com.inzynierka2k24.EventType;
import com.inzynierka2k24.GetMessageStatusResponse;
import com.inzynierka2k24.MessageStatus;
import com.inzynierka2k24.Status;
import com.inzynierka2k24.messagingservice.model.MessageDetails;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Set;
import org.junit.jupiter.api.Test;

class GetMessageStatusResponseGeneratorTest {

  @Test
  void shouldGetResponse() {
    var time = Instant.EPOCH.plus(2, ChronoUnit.DAYS);
    var details =
        Set.of(
            new MessageDetails(1L, EventType.RESERVATION, Instant.EPOCH, Status.STORED),
            new MessageDetails(2L, EventType.RENOVATION, time, Status.SUCCESS));
    var expected =
        GetMessageStatusResponse.newBuilder()
            .addAllMessageStatus(
                Set.of(
                    MessageStatus.newBuilder()
                        .setStatus(Status.STORED)
                        .setMessageTime(Timestamp.getDefaultInstance())
                        .build(),
                    MessageStatus.newBuilder()
                        .setStatus(Status.SUCCESS)
                        .setMessageTime(
                            Timestamp.newBuilder().setSeconds(time.getEpochSecond()).build())
                        .build()))
            .build();

    assertEquals(expected, GetMessageStatusResponseGenerator.getResponse(details));
  }
}
