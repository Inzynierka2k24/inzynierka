package com.inzynierka2k24.apiserver.grpc.util;

import static com.inzynierka2k24.apiserver.grpc.util.TimeConverter.toInstant;
import static com.inzynierka2k24.apiserver.grpc.util.TimeConverter.toProtoTimestamp;
import static org.junit.jupiter.api.Assertions.*;

import com.google.protobuf.Timestamp;
import java.time.Instant;
import org.junit.jupiter.api.Test;

class TimeConverterTest {

  @Test
  void shouldConvertToInstant() {
    // Given
    var epochSeconds = 1000000;
    var expected = Instant.ofEpochSecond(epochSeconds);
    var timestamp = Timestamp.newBuilder().setSeconds(epochSeconds).build();

    // When/Then
    assertEquals(expected, toInstant(timestamp));
  }

  @Test
  void shouldConvertToProtoTimestamp() {
    // Given
    var epochSeconds = 1000000;
    var instant = Instant.ofEpochSecond(epochSeconds);
    var expected = Timestamp.newBuilder().setSeconds(epochSeconds).build();

    // When/Then
    assertEquals(expected, toProtoTimestamp(instant));
  }
}
