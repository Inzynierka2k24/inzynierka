package com.inzynierka2k24.messagingservice.server.request;

import static org.junit.jupiter.api.Assertions.*;

import com.google.protobuf.Timestamp;
import com.inzynierka2k24.EventData;
import com.inzynierka2k24.Message;
import com.inzynierka2k24.MessageType;
import com.inzynierka2k24.SendMessageRequest;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import org.junit.jupiter.api.Test;

public class RequestValidatorTest {

  private final RequestValidator requestValidator = new RequestValidator();

  @Test
  public void shouldReturnNullWhenRequestWithValidMail() {
    // Given
    SendMessageRequest request =
        SendMessageRequest.newBuilder()
            .setMessage(
                Message.newBuilder()
                    .setReceiver("test@example.com")
                    .setMessageType(MessageType.MAIL)
                    .build())
            .setEventData(
                EventData.newBuilder()
                    .setEventTime(
                        Timestamp.newBuilder()
                            .setSeconds(Instant.now().plus(Duration.ofDays(1)).getEpochSecond())
                            .setNanos(0)
                            .build())
                    .build())
            .build();

    // When
    ValidationError result = requestValidator.validate(request);

    // Then
    assertNull(result);
  }

  @Test
  public void shouldReturnNullWhenRequestWithValidNumber() {
    // Given
    SendMessageRequest request =
        SendMessageRequest.newBuilder()
            .setMessage(
                Message.newBuilder()
                    .setReceiver("123456789")
                    .setMessageType(MessageType.SMS)
                    .build())
            .setEventData(
                EventData.newBuilder()
                    .setEventTime(
                        Timestamp.newBuilder()
                            .setSeconds(Instant.now().plus(Duration.ofDays(1)).getEpochSecond())
                            .setNanos(0)
                            .build())
                    .build())
            .build();

    // When
    ValidationError result = requestValidator.validate(request);

    // Then
    assertNull(result);
  }

  // Validates a message with a valid email receiver and SMS message type
  @Test
  public void shouldReturnErrorWhenInvalidMail() {
    // Given
    SendMessageRequest request =
        SendMessageRequest.newBuilder()
            .setMessage(
                Message.newBuilder()
                    .setReceiver("abc.com@test")
                    .setMessageType(MessageType.MAIL)
                    .build())
            .setEventData(
                EventData.newBuilder()
                    .setEventTime(
                        Timestamp.newBuilder()
                            .setSeconds(Instant.now().plus(Duration.ofDays(1)).getEpochSecond())
                            .setNanos(0)
                            .build())
                    .build())
            .build();

    // When
    ValidationError result = requestValidator.validate(request);

    // Then
    assertEquals(ValidationError.INVALID_MAIL, result);
  }

  // Validates a message with a valid phone number receiver and MAIL message type
  @Test
  public void shouldReturnErrorWhenInvalidNumber() {
    // Given
    SendMessageRequest request =
        SendMessageRequest.newBuilder()
            .setMessage(
                Message.newBuilder()
                    .setReceiver("1234567890")
                    .setMessageType(MessageType.SMS)
                    .build())
            .setEventData(
                EventData.newBuilder()
                    .setEventTime(
                        Timestamp.newBuilder()
                            .setSeconds(Instant.now().plus(Duration.ofDays(1)).getEpochSecond())
                            .setNanos(0)
                            .build())
                    .build())
            .build();

    // When
    ValidationError result = requestValidator.validate(request);

    // Then
    assertEquals(ValidationError.INVALID_PHONE_NUMBER, result);
  }

  @Test
  public void shouldReturnErrorWhenEmptyReceiver() {
    // Given
    SendMessageRequest request =
        SendMessageRequest.newBuilder()
            .setMessage(
                Message.newBuilder().setReceiver("").setMessageType(MessageType.MAIL).build())
            .setEventData(
                EventData.newBuilder()
                    .setEventTime(
                        Timestamp.newBuilder()
                            .setSeconds(Instant.now().plus(Duration.ofDays(1)).getEpochSecond())
                            .setNanos(0)
                            .build())
                    .build())
            .build();

    // When
    ValidationError result = requestValidator.validate(request);

    // Then
    assertEquals(ValidationError.EMPTY_RECEIVER, result);
  }

  @Test
  public void shouldReturnErrorWhenInvalidReceiverFormat() {
    // Given
    SendMessageRequest request =
        SendMessageRequest.newBuilder()
            .setMessage(Message.newBuilder().setReceiver("test").setMessageTypeValue(-1).build())
            .setEventData(
                EventData.newBuilder()
                    .setEventTime(
                        Timestamp.newBuilder()
                            .setSeconds(Instant.now().plus(Duration.ofDays(1)).getEpochSecond())
                            .setNanos(0)
                            .build())
                    .build())
            .build();

    // When
    ValidationError result = requestValidator.validate(request);

    // Then
    assertEquals(ValidationError.INVALID_RECEIVER_FORMAT, result);
  }

  @Test
  public void shouldReturnErrorWhenInvalidDate() {
    // Given
    SendMessageRequest request =
        SendMessageRequest.newBuilder()
            .setMessage(
                Message.newBuilder()
                    .setReceiver("test@example.com")
                    .setMessageType(MessageType.MAIL)
                    .build())
            .setEventData(
                EventData.newBuilder()
                    .setEventTime(
                        Timestamp.newBuilder()
                            .setSeconds(Instant.now().minus(Duration.ofDays(1)).getEpochSecond())
                            .setNanos(0)
                            .build())
                    .build())
            .build();

    // When
    ValidationError result = requestValidator.validate(request);

    // Then
    assertEquals(ValidationError.INVALID_DATE, result);
  }

  @Test
  void shouldReturnTrueWhenEventDateIsInLessThen14DaysInFuture() {
    assertTrue(
        requestValidator.shouldBeSentNow(
            Timestamp.newBuilder()
                .setSeconds(Instant.now().plus(3, ChronoUnit.DAYS).getEpochSecond())
                .build()));
  }

  @Test
  void shouldReturnTrueWhenEventDateIsInMoreThen14DaysInFuture() {
    assertFalse(
        requestValidator.shouldBeSentNow(
            Timestamp.newBuilder()
                .setSeconds(Instant.now().plus(20, ChronoUnit.DAYS).getEpochSecond())
                .build()));
  }
}
