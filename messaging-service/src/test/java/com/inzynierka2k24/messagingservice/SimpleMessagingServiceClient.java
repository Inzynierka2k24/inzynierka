package com.inzynierka2k24.messagingservice;

import com.inzynierka2k24.Message;
import com.inzynierka2k24.MessageType;
import com.inzynierka2k24.MessagingServiceGrpc;
import com.inzynierka2k24.SendMessageRequest;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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
    var request =
        SendMessageRequest.newBuilder()
            .setMessage(
                Message.newBuilder()
                    .setReceiver("test@mail.com")
                    .setContent("Sending mail...")
                    .setMessageType(MessageType.MAIL)
                    .build())
            .build();
    var start = Instant.now();
    var response = blockingStub.sendMessage(request);

    System.out.printf(
        "Response: %s. Connection active for %s", response, Duration.between(start, Instant.now()));
  }
}
