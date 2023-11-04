package com.inzynierka2k24.external.server.request;

import static io.grpc.Status.Code.INVALID_ARGUMENT;

import io.grpc.Status;
import io.grpc.Status.Code;
import lombok.Getter;

@Getter
public enum ValidationError {
  INVALID_DATE(INVALID_ARGUMENT, "Dates cannot be in the past"),
  EMPTY_STRING(INVALID_ARGUMENT, "String field cannot be empty");

  private final Status grpcStatus;

  ValidationError(Code code, String message) {
    grpcStatus = Status.fromCode(code).withDescription(message);
  }

  public String getMessage() {
    return grpcStatus.getDescription();
  }
}
