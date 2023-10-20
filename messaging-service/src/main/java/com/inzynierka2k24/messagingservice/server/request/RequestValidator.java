package com.inzynierka2k24.messagingservice.server.request;

import static com.inzynierka2k24.messagingservice.util.ProtoTimestampToInstantConverter.toInstant;

import com.google.protobuf.Timestamp;
import java.time.Duration;
import java.time.Instant;
import org.springframework.stereotype.Service;

@Service
public class RequestValidator {

  private static final long DAYS_BEFORE_SENDING = 14;

  public boolean shouldBeSentNow(Timestamp eventDate) {
    return Duration.between(toInstant(eventDate), Instant.now()).toDays() < DAYS_BEFORE_SENDING;
  }
}
