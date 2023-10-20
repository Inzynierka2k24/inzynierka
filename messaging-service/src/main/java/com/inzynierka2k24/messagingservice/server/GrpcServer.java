package com.inzynierka2k24.messagingservice.server;

import com.google.protobuf.Timestamp;
import com.inzynierka2k24.GetMessageStatusRequest;
import com.inzynierka2k24.GetMessageStatusResponse;
import com.inzynierka2k24.MessageStatus;
import com.inzynierka2k24.MessagingServiceGrpc;
import com.inzynierka2k24.SendMessageRequest;
import com.inzynierka2k24.SendMessageResponse;
import com.inzynierka2k24.Status;
import com.inzynierka2k24.messagingservice.server.request.RequestConverter;
import com.inzynierka2k24.messagingservice.service.MessageStatusService;
import com.inzynierka2k24.messagingservice.service.messaging.MessageSenderProvider;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lognet.springboot.grpc.GRpcService;

@GRpcService
@Slf4j
@RequiredArgsConstructor
public class GrpcServer extends MessagingServiceGrpc.MessagingServiceImplBase {

  private final MessageSenderProvider senderProvider;
  private final MessageStatusService messageStatusService;

  @Override
  public void sendMessage(
      SendMessageRequest request, StreamObserver<SendMessageResponse> responseObserver) {
    var message = request.getMessage();
    log.info("Message sent to {}. Content: {}", message.getReceiver(), message.getContent());

    var status =
        senderProvider
            .getMessageSender(message.getMessageType())
            .sentMessage(RequestConverter.convertToMessage(message));

    messageStatusService.save(null);

    responseObserver.onNext(SendMessageResponse.newBuilder().setStatus(status).build());
    responseObserver.onCompleted();
  }

  @Override
  public void getMessageStatus(
      GetMessageStatusRequest request, StreamObserver<GetMessageStatusResponse> responseObserver) {
    log.info(
        "Receiver: {}, eventId: {}", request.getReceiver(), request.getEventData().getEventId());

    var statuses = messageStatusService.get(request.getReceiver());

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
