package com.inzynierka2k24.messagingservice.server.request;

public class InvalidRequestException extends Exception {

  public InvalidRequestException(ValidationError validationError) {
    super(
        String.join(
            " - ",
            validationError.getGrpcStatus().toString(),
            validationError.name(),
            validationError.getMessage()));
  }
}
