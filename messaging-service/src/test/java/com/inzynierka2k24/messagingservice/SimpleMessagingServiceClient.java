package com.inzynierka2k24.messagingservice;

import com.google.protobuf.Timestamp;
import com.inzynierka2k24.EventData;
import com.inzynierka2k24.EventType;
import com.inzynierka2k24.GetMessageStatusRequest;
import com.inzynierka2k24.Message;
import com.inzynierka2k24.MessageType;
import com.inzynierka2k24.MessagingServiceGrpc;
import com.inzynierka2k24.SendMessageRequest;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SimpleMessagingServiceClient {
  private static final String API_URL = "localhost";
  private static final int PORT = 6565;

  public static void main(String[] args) {
    ManagedChannel channel =
        ManagedChannelBuilder.forAddress(API_URL, PORT)
            .usePlaintext()
            .keepAliveTime(5, TimeUnit.SECONDS)
            .executor(Executors.newFixedThreadPool(1))
            .build();

    new SimpleMessagingServiceClient(channel);
  }

  public SimpleMessagingServiceClient(ManagedChannel channel) {
    var blockingStub = MessagingServiceGrpc.newBlockingStub(channel);
    sendMessage(blockingStub);
    getStatus(blockingStub);
  }

  private void sendMessage(MessagingServiceGrpc.MessagingServiceBlockingStub blockingStub) {
    var sendMessageRequest =
        SendMessageRequest.newBuilder()
            .setMessage(
                Message.newBuilder()
                    .setReceiver("apartmanager404@gmail.com")
                    .setSubject("Cleaning service for apartment xyz")
                    .setContent("I'd like to order cleaning service for apartment xyz.")
                    .setMessageType(MessageType.MAIL)
                    .build())
            .setEventData(
                EventData.newBuilder()
                    .setEventId(1L)
                    .setEventType(EventType.RESERVATION)
                    .setEventTime(
                        Timestamp.newBuilder()
                            .setSeconds(Instant.now().plus(1, ChronoUnit.DAYS).getEpochSecond())
                            .build())
                    .build())
            .build();
    //    var sendMessageRequest =
    //        SendMessageRequest.newBuilder()
    //            .setMessage(
    //                Message.newBuilder()
    //                    .setReceiver("797955650")
    //                    .setContent("Sending sms...")
    //                    .setMessageType(MessageType.SMS)
    //                    .build())
    //            .build();
    var sendMessageStart = Instant.now();
    var sendMessageResponse = blockingStub.sendMessage(sendMessageRequest);

    log.info(
        "SendMessage response: {} Connection active for {}",
        sendMessageResponse,
        Duration.between(sendMessageStart, Instant.now()));
  }

  private void getStatus(MessagingServiceGrpc.MessagingServiceBlockingStub blockingStub) {
    var getMessageStatusRequest =
        GetMessageStatusRequest.newBuilder()
            .setReceiver("apartmanager404@gmail.com")
            .setEventData(
                EventData.newBuilder().setEventType(EventType.RESERVATION).setEventId(1L).build())
            .build();
    var getMessageStatusStart = Instant.now();
    var getMessageStatusResponse = blockingStub.getMessageStatus(getMessageStatusRequest);

    log.info(
        "GetMessageStatus response: {}. Connection active for {}",
        getMessageStatusResponse,
        Duration.between(getMessageStatusStart, Instant.now()));
  }
}
