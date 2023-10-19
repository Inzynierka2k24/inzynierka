package com.inzynierka2k24.messagingservice.server;

import com.google.protobuf.Timestamp;
import com.inzynierka2k24.GetMessageStatusRequest;
import com.inzynierka2k24.GetMessageStatusResponse;
import com.inzynierka2k24.MessageStatus;
import com.inzynierka2k24.MessagingServiceGrpc;
import com.inzynierka2k24.SendMessageRequest;
import com.inzynierka2k24.SendMessageResponse;
import com.inzynierka2k24.Status;
import com.inzynierka2k24.messagingservice.server.request.MessageConverter;
import com.inzynierka2k24.messagingservice.service.messaging.MessageSenderProvider;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.lognet.springboot.grpc.GRpcService;

@GRpcService
@RequiredArgsConstructor
public class GrpcServer extends MessagingServiceGrpc.MessagingServiceImplBase {

  private final MessageSenderProvider senderProvider;

  @Override
  public void sendMessage(
      SendMessageRequest request, StreamObserver<SendMessageResponse> responseObserver) {
    var message = request.getMessage();
    System.out.printf(
        "Message sent to %s. Content: %s\n", message.getReceiver(), message.getContent());

    senderProvider
        .getMessageSender(message.getMessageType())
        .sentMessage(MessageConverter.convert(message));

    responseObserver.onNext(SendMessageResponse.newBuilder().setStatus(Status.SUCCESS).build());
    responseObserver.onCompleted();
  }

  @Override
  public void getMessageStatus(
      GetMessageStatusRequest request, StreamObserver<GetMessageStatusResponse> responseObserver) {
    System.out.printf(
        "Receiver: %s, eventId: %s\n", request.getReceiver(), request.getEventData().getEventId());

    responseObserver.onNext(
        GetMessageStatusResponse.newBuilder()
            .addMessageStatus(
                MessageStatus.newBuilder()
                    .setStatus(Status.SUCCESS)
                    .setMessageTime(Timestamp.newBuilder().getDefaultInstanceForType()))
            .build());
    responseObserver.onCompleted();
  }
}
