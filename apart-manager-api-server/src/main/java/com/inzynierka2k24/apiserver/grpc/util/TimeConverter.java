package com.inzynierka2k24.apiserver.grpc.util;

import com.google.protobuf.Timestamp;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TimeConverter {

  public static Instant toInstant(Timestamp timestamp) {
    return Instant.ofEpochSecond(timestamp.getSeconds(), timestamp.getNanos());
  }

  public static Timestamp toProtoTimestamp(Instant instant) {
    return Timestamp.newBuilder().setSeconds(instant.getEpochSecond()).build();
  }
}
