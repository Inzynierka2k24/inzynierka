package com.inzynierka2k24.apiserver.grpc.integration;

import com.inzynierka2k24.ExternalIntegrationServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExternalIntegrationServiceConfig {

  @Bean
  public ExternalIntegrationServiceGrpc.ExternalIntegrationServiceBlockingStub blockingStub(
      ExternalIntegrationServiceProperties properties) {
    ManagedChannel channel =
        ManagedChannelBuilder.forAddress(properties.url(), properties.port())
            .usePlaintext()
            .keepAliveTime(5, TimeUnit.SECONDS)
            .executor(Executors.newFixedThreadPool(1))
            .build();

    return ExternalIntegrationServiceGrpc.newBlockingStub(channel);
  }
}
