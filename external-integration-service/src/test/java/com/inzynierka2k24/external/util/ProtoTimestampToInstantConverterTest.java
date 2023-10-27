package com.inzynierka2k24.external.util;

import static com.inzynierka2k24.external.util.ProtoTimestampToInstantConverter.toInstant;
import static org.junit.jupiter.api.Assertions.*;

import com.google.protobuf.Timestamp;
import java.time.Instant;
import org.junit.jupiter.api.Test;

class ProtoTimestampToInstantConverterTest {

  @Test
  void shouldConvertToInstant() {
    // Given
    var epochSeconds = 1000000;
    var expected = Instant.ofEpochSecond(epochSeconds);
    var timestamp = Timestamp.newBuilder().setSeconds(epochSeconds).build();

    // When/Then
    assertEquals(expected, toInstant(timestamp));
  }
}
