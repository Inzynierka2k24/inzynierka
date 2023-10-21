package com.inzynierka2k24.messagingservice.server.request;

import static com.inzynierka2k24.messagingservice.util.ProtoTimestampToInstantConverter.toInstant;

import com.google.protobuf.Timestamp;
import com.inzynierka2k24.MessageType;
import com.inzynierka2k24.SendMessageRequest;
import java.time.Duration;
import java.time.Instant;
import java.util.regex.Pattern;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.stereotype.Service;

@Service
public class RequestValidator {

  private static final long DAYS_BEFORE_SENDING = 14;
  private static final Pattern PHONE_NUMBER_PATTERN = Pattern.compile("^\\d{9}$");

  public ValidationError validate(SendMessageRequest request) {
    var message = request.getMessage();
    var receiverValidation = validateReceiver(message.getReceiver(), message.getMessageType());

    if (receiverValidation != null) {
      return receiverValidation;
    }

    return validateDates(request.getEventData().getEventTime());
  }

  public boolean shouldBeSentNow(Timestamp eventDate) {
    return Duration.between(toInstant(eventDate), Instant.now()).toDays() < DAYS_BEFORE_SENDING;
  }

  private ValidationError validateReceiver(String receiver, MessageType messageType) {
    if (receiver.isBlank()) {
      return ValidationError.EMPTY_RECEIVER;
    }

    return switch (messageType) {
      case MAIL -> validateMail(receiver);
      case SMS -> validatePhoneNumber(receiver);
      default -> ValidationError.INVALID_RECEIVER_FORMAT;
    };
  }

  private ValidationError validateMail(String mail) {
    return EmailValidator.getInstance().isValid(mail) ? null : ValidationError.INVALID_MAIL;
  }

  private ValidationError validatePhoneNumber(String number) {
    return PHONE_NUMBER_PATTERN.matcher(number).matches()
        ? null
        : ValidationError.INVALID_PHONE_NUMBER;
  }

  private ValidationError validateDates(Timestamp eventTime) {
    return toInstant(eventTime).isAfter(Instant.now()) ? null : ValidationError.INVALID_DATE;
  }
}
