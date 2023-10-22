package com.inzynierka2k24.messagingservice.server;

import static org.mockito.Mockito.*;

import com.google.protobuf.Timestamp;
import com.inzynierka2k24.*;
import com.inzynierka2k24.messagingservice.server.request.InvalidRequestException;
import com.inzynierka2k24.messagingservice.server.request.RequestValidator;
import com.inzynierka2k24.messagingservice.service.MessageStatusService;
import com.inzynierka2k24.messagingservice.service.messaging.EmailSender;
import com.inzynierka2k24.messagingservice.service.messaging.MessageSenderProvider;
import com.inzynierka2k24.messagingservice.service.messaging.SmsSender;
import io.grpc.stub.StreamObserver;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import org.junit.jupiter.api.Test;

public class GrpcServerTest {

  private final EmailSender emailSender = mock(EmailSender.class);
  private final SmsSender smsSender = mock(SmsSender.class);
  private final MessageStatusService statusService = mock(MessageStatusService.class);
  private final MessageSenderProvider provider = new MessageSenderProvider(emailSender, smsSender);
  private final GrpcServer grpcServer =
      new GrpcServer(new RequestValidator(), provider, statusService);

  @Test
  public void shouldSuccessfullySendMessageWhenRequestWithValidInputAndEventTimeWithin14Days() {
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
  public void shouldSuccessfulGetMessageStatusRequestWithValidInput() {
    // Given
    GetMessageStatusRequest request =
        GetMessageStatusRequest.newBuilder()
            .setReceiver("receiver@example.com")
            .setEventData(EventData.newBuilder().setEventId(123456L).build())
            .build();

    StreamObserver<GetMessageStatusResponse> responseObserver = mock(StreamObserver.class);

    // When
    grpcServer.getMessageStatus(request, responseObserver);

    // Then
    verify(responseObserver)
        .onNext(
            GetMessageStatusResponse.newBuilder()
                .addMessageStatus(
                    MessageStatus.newBuilder()
                        .setStatus(Status.SUCCESS)
                        .setMessageTime(Timestamp.newBuilder().getDefaultInstanceForType())
                        .build())
                .build());
    verify(responseObserver).onCompleted();
    verifyNoMoreInteractions(responseObserver);
  }

  @Test
  public void shouldThrowInvalidRequestExceptionWhenValidationFails() {
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
  public void shouldNotSentMessageWhenEventTimeMoreThan14DaysInFuture() {
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

    // When
    grpcServer.sendMessage(request, responseObserver);

    // Then
    verifyNoInteractions(provider.getMessageSender(MessageType.MAIL));
    verify(statusService).save(any());
    verify(responseObserver)
        .onNext(SendMessageResponse.newBuilder().setStatus(Status.STORED).build());
    verify(responseObserver).onCompleted();
    verifyNoMoreInteractions(responseObserver);
  }
}
