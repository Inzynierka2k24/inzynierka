package com.inzynierka2k24.messagingservice.server;

import com.inzynierka2k24.GetMessageStatusRequest;
import com.inzynierka2k24.GetMessageStatusResponse;
import com.inzynierka2k24.MessagingServiceGrpc;
import com.inzynierka2k24.SendMessageRequest;
import com.inzynierka2k24.SendMessageResponse;
import com.inzynierka2k24.Status;
import com.inzynierka2k24.messagingservice.server.request.InvalidRequestException;
import com.inzynierka2k24.messagingservice.server.request.RequestConverter;
import com.inzynierka2k24.messagingservice.server.request.RequestValidator;
import com.inzynierka2k24.messagingservice.server.response.GetMessageStatusResponseGenerator;
import com.inzynierka2k24.messagingservice.service.MessageService;
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
  private final MessageService messageService;

  @Override
  public void sendMessage(
      SendMessageRequest request, StreamObserver<SendMessageResponse> responseObserver) {
    var status = Status.PENDING;
    var validationError = validator.validate(request);

    if (validationError != null) {
      responseObserver.onError(new InvalidRequestException(validationError));
      return;
    }

    var message = RequestConverter.convertToMessage(request);

    if (validator.shouldBeSentNow(request.getEventData().getEventTime())) {
      status = senderProvider.getMessageSender(message.type()).sendMessage(message.content());
    }

    var savingStatus = messageService.save(message);

    responseObserver.onNext(
        SendMessageResponse.newBuilder()
            .setStatus(status == Status.SUCCESS ? status : savingStatus)
            .build());
    responseObserver.onCompleted();
  }

  @Override
  public void getMessageStatus(
      GetMessageStatusRequest request, StreamObserver<GetMessageStatusResponse> responseObserver) {
    var event = request.getEventData();
    log.info(
        "Receiver: {}, eventId: {}", request.getReceiver(), request.getEventData().getEventId());

    responseObserver.onNext(
        GetMessageStatusResponseGenerator.getResponse(
            messageService.get(request.getReceiver(), event.getEventId(), event.getEventType())));
    responseObserver.onCompleted();
  }
}
