package com.inzynierka2k24.messagingservice.server;

import com.google.protobuf.Timestamp;
import com.inzynierka2k24.GetMessageStatusRequest;
import com.inzynierka2k24.GetMessageStatusResponse;
import com.inzynierka2k24.MessageStatus;
import com.inzynierka2k24.MessagingServiceGrpc;
import com.inzynierka2k24.SendMessageRequest;
import com.inzynierka2k24.SendMessageResponse;
import com.inzynierka2k24.Status;
import com.inzynierka2k24.messagingservice.server.request.InvalidRequestException;
import com.inzynierka2k24.messagingservice.server.request.RequestConverter;
import com.inzynierka2k24.messagingservice.server.request.RequestValidator;
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

  private final RequestValidator validator;
  private final MessageSenderProvider senderProvider;
  private final MessageStatusService messageStatusService;

  @Override
  public void sendMessage(
      SendMessageRequest request, StreamObserver<SendMessageResponse> responseObserver) {
    var message = request.getMessage();
    var status = Status.STORED;
    var validationError = validator.validate(request);

    if (validationError != null) {
      responseObserver.onError(new InvalidRequestException(validationError));
    }

    if (validator.shouldBeSentNow(request.getEventData().getEventTime())) {
      status =
          senderProvider
              .getMessageSender(message.getMessageType())
              .sentMessage(RequestConverter.convertToMessage(message));
    }

    messageStatusService.save(null); // TODO save status in database

    responseObserver.onNext(SendMessageResponse.newBuilder().setStatus(status).build());
    responseObserver.onCompleted();
  }

  @Override
  public void getMessageStatus(
      GetMessageStatusRequest request, StreamObserver<GetMessageStatusResponse> responseObserver) {
    log.info(
        "Receiver: {}, eventId: {}", request.getReceiver(), request.getEventData().getEventId());

    var statuses =
        messageStatusService.get(request.getReceiver()); // TODO get statuses from database

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
