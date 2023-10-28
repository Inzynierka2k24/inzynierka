package com.inzynierka2k24.messagingservice.server;

import static org.mockito.Mockito.*;

import com.google.protobuf.Timestamp;
import com.inzynierka2k24.*;
import com.inzynierka2k24.messagingservice.model.MessageDetails;
import com.inzynierka2k24.messagingservice.server.request.InvalidRequestException;
import com.inzynierka2k24.messagingservice.server.request.RequestValidator;
import com.inzynierka2k24.messagingservice.server.response.GetMessageStatusResponseGenerator;
import com.inzynierka2k24.messagingservice.service.MessageService;
import com.inzynierka2k24.messagingservice.service.messaging.EmailSender;
import com.inzynierka2k24.messagingservice.service.messaging.MessageSenderProvider;
import com.inzynierka2k24.messagingservice.service.messaging.SmsSender;
import io.grpc.stub.StreamObserver;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Set;
import org.junit.jupiter.api.Test;

class GrpcServerTest {

  private final EmailSender emailSender = mock(EmailSender.class);
  private final SmsSender smsSender = mock(SmsSender.class);
  private final MessageService messageService = mock(MessageService.class);
  private final MessageSenderProvider provider = new MessageSenderProvider(emailSender, smsSender);
  private final GrpcServer grpcServer =
      new GrpcServer(new RequestValidator(), provider, messageService);

  @Test
  void shouldSuccessfullySendMessageWhenRequestWithValidInputAndEventTimeWithin14Days() {
    // Given
    SendMessageRequest request =
        SendMessageRequest.newBuilder()
            .setMessage(
                Message.newBuilder()
                    .setReceiver("receiver@example.com")
                    .setSubject("Test Subject")
                    .setContent("Test Content")
                    .setMessageType(MessageType.MAIL)
                    .build())
            .setEventData(
                EventData.newBuilder()
                    .setEventTime(
                        Timestamp.newBuilder()
                            .setSeconds(Instant.now().plus(1, ChronoUnit.DAYS).getEpochSecond())
                            .build())
                    .build())
            .build();

    StreamObserver<SendMessageResponse> responseObserver = mock(StreamObserver.class);
    when(emailSender.sendMessage(any())).thenReturn(Status.SUCCESS);

    // When
    grpcServer.sendMessage(request, responseObserver);

    // Then
    verify(responseObserver)
        .onNext(SendMessageResponse.newBuilder().setStatus(Status.SUCCESS).build());
    verify(responseObserver).onCompleted();
    verifyNoMoreInteractions(responseObserver);
  }

  @Test
  void shouldSuccessfulGetMessageStatusRequestWithValidInput() {
    // Given
    var receiver = "receiver@example.com";
    var eventId = 123456L;
    var eventType = EventType.RESERVATION;
    GetMessageStatusRequest request =
        GetMessageStatusRequest.newBuilder()
            .setReceiver(receiver)
            .setEventData(
                EventData.newBuilder().setEventId(eventId).setEventType(eventType).build())
            .build();
    Set<MessageDetails> expectedStatuses =
        Set.of(
            new MessageDetails(1L, EventType.RESERVATION, Instant.EPOCH, Status.STORED),
            new MessageDetails(2L, EventType.RESERVATION, Instant.EPOCH, Status.SUCCESS));

    StreamObserver<GetMessageStatusResponse> responseObserver = mock(StreamObserver.class);
    when(messageService.get(receiver, eventId, eventType)).thenReturn(expectedStatuses);

    // When
    grpcServer.getMessageStatus(request, responseObserver);

    // Then
    verify(responseObserver)
        .onNext(GetMessageStatusResponseGenerator.getResponse(expectedStatuses));
    verify(responseObserver).onCompleted();
    verifyNoMoreInteractions(responseObserver);
  }

  @Test
  void shouldThrowInvalidRequestExceptionWhenValidationFails() {
    // Given
    SendMessageRequest request =
        SendMessageRequest.newBuilder()
            .setMessage(
                Message.newBuilder()
                    .setReceiver("")
                    .setSubject("Test Subject")
                    .setContent("Test Content")
                    .setMessageType(MessageType.MAIL)
                    .build())
            .setEventData(
                EventData.newBuilder()
                    .setEventTime(
                        Timestamp.newBuilder()
                            .setSeconds(Instant.now().plus(1, ChronoUnit.DAYS).getEpochSecond())
                            .build())
                    .build())
            .build();

    StreamObserver<SendMessageResponse> responseObserver = mock(StreamObserver.class);

    // When
    grpcServer.sendMessage(request, responseObserver);

    // Then
    verify(responseObserver).onError(any(InvalidRequestException.class));
    verifyNoMoreInteractions(responseObserver);
  }

  @Test
  void shouldNotSentMessageWhenEventTimeMoreThan14DaysInFuture() {
    // Given
    SendMessageRequest request =
        SendMessageRequest.newBuilder()
            .setMessage(
                Message.newBuilder()
                    .setReceiver("receiver@example.com")
                    .setSubject("Test Subject")
                    .setContent("Test Content")
                    .setMessageType(MessageType.MAIL)
                    .build())
            .setEventData(
                EventData.newBuilder()
                    .setEventTime(
                        Timestamp.newBuilder()
                            .setSeconds(Instant.now().plus(15, ChronoUnit.DAYS).getEpochSecond())
                            .build())
                    .build())
            .build();

    StreamObserver<SendMessageResponse> responseObserver = mock(StreamObserver.class);
    when(messageService.save(any(com.inzynierka2k24.messagingservice.model.Message.class)))
        .thenReturn(Status.STORED);

    // When
    grpcServer.sendMessage(request, responseObserver);

    // Then
    verifyNoInteractions(provider.getMessageSender(MessageType.MAIL));
    verify(messageService).save(any());
    verify(responseObserver)
        .onNext(SendMessageResponse.newBuilder().setStatus(Status.STORED).build());
    verify(responseObserver).onCompleted();
    verifyNoMoreInteractions(responseObserver);
  }
}
