package com.inzynierka2k24.messagingservice.server.request;

import static io.grpc.Status.Code.INVALID_ARGUMENT;

import io.grpc.Status;
import io.grpc.Status.Code;
import lombok.Getter;

@Getter
public enum ValidationError {
  INVALID_DATE(INVALID_ARGUMENT, "The date cannot be in the past"),
  EMPTY_RECEIVER(INVALID_ARGUMENT, "Receiver field cannot be empty"),
  INVALID_NUMBER(INVALID_ARGUMENT, "Receiver field contains invalid phone number"),
  INVALID_MAIL(INVALID_ARGUMENT, "Receiver field contains invalid mail");

  private final Status grpcStatus;

  ValidationError(Code code, String message) {
    grpcStatus = Status.fromCode(code).withDescription(message);
  }

  public String getMessage() {
    return grpcStatus.getDescription();
  }
}
