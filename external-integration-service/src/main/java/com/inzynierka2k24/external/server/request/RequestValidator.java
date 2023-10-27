package com.inzynierka2k24.external.server.request;

import static com.inzynierka2k24.external.util.ProtoTimestampToInstantConverter.toInstant;

import com.google.protobuf.Timestamp;
import java.time.Instant;
import org.springframework.stereotype.Service;

@Service
public class RequestValidator {

  public ValidationError validate(Object request) {
    return null;
    //    var message = request.getMessage();
    //    var receiverValidation = validateReceiver(message.getReceiver(),
    // message.getMessageType());
    //
    //    if (receiverValidation != null) {
    //      return receiverValidation;
    //    }
    //
    //    return validateDates(request.getEventData().getEventTime());
  }

  private ValidationError validateDates(Timestamp eventTime) {
    return toInstant(eventTime).isAfter(Instant.now()) ? null : ValidationError.INVALID_DATE;
  }
}
