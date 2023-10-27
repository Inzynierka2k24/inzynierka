package com.inzynierka2k24.external.server;

import static org.mockito.Mockito.*;

import com.inzynierka2k24.*;
import com.inzynierka2k24.external.server.request.RequestValidator;

class GrpcServerTest {

  private final GrpcServer grpcServer = new GrpcServer(new RequestValidator());
}
