package com.inzynierka2k24.messagingservice.server;

import com.inzynierka2k24.MessagingServiceGrpc;
import com.inzynierka2k24.SendMessageRequest;
import com.inzynierka2k24.SendMessageResponse;
import com.inzynierka2k24.Status;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.lognet.springboot.grpc.GRpcService;

@GRpcService
@RequiredArgsConstructor
public class GrpcServer extends MessagingServiceGrpc.MessagingServiceImplBase {

  @Override
  public void sendMessage(
      SendMessageRequest request, StreamObserver<SendMessageResponse> responseObserver) {
    var message = request.getMessage();
    System.out.printf(
        "Message sent to %s. Content: %s\n", message.getReceiver(), message.getContent());

    responseObserver.onNext(SendMessageResponse.newBuilder().setStatus(Status.SUCCESS).build());
    responseObserver.onCompleted();
  }
}
