package com.inzynierka2k24.apiserver.grpc.integration;

import static org.junit.jupiter.api.Assertions.*;

import com.inzynierka2k24.apiserver.grpc.integration.config.ExternalIntegrationServiceConfig;
import org.junit.jupiter.api.Test;

class MessagingServiceConfigTest {

  @Test
  void shouldCreateBlockingStub() {
    assertDoesNotThrow(
        () ->
            new ExternalIntegrationServiceConfig()
                .blockingStub(new ExternalIntegrationServiceProperties("localhost", 6565)));
  }
}
