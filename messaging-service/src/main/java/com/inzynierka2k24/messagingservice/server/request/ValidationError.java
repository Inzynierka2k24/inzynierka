package com.inzynierka2k24.messagingservice.server.request;

import static io.grpc.Status.Code.INVALID_ARGUMENT;

import io.grpc.Status;
import io.grpc.Status.Code;
import lombok.Getter;

@Getter
public enum ValidationError {
  INVALID_DATE(INVALID_ARGUMENT, "Dates cannot be in the past"),
  EMPTY_RECEIVER(INVALID_ARGUMENT, "Receiver field cannot be empty"),
  INVALID_PHONE_NUMBER(INVALID_ARGUMENT, "Receiver field contains invalid phone number"),
  INVALID_MAIL(INVALID_ARGUMENT, "Receiver field contains invalid mail"),
  INVALID_RECEIVER_FORMAT(INVALID_ARGUMENT, "Invalid receiver format");

  private final Status grpcStatus;

  ValidationError(Code code, String message) {
    grpcStatus = Status.fromCode(code).withDescription(message);
  }

  public String getMessage() {
    return grpcStatus.getDescription();
  }
}
