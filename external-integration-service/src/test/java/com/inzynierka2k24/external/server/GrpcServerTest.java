package com.inzynierka2k24.external.server;

import static org.mockito.Mockito.*;

import com.inzynierka2k24.*;
import com.inzynierka2k24.external.server.request.RequestValidator;
import com.inzynierka2k24.external.service.IntegrationService;

class GrpcServerTest {

  private final IntegrationService integrationService = mock(IntegrationService.class);
  private final GrpcServer grpcServer = new GrpcServer(new RequestValidator(), integrationService);
}
