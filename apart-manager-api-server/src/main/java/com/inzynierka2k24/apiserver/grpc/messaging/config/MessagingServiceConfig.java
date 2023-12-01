package com.inzynierka2k24.apiserver.grpc.messaging.config;

import com.inzynierka2k24.MessagingServiceGrpc;
import com.inzynierka2k24.apiserver.grpc.messaging.MessagingServiceProperties;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessagingServiceConfig {

  @Bean
  public MessagingServiceGrpc.MessagingServiceBlockingStub messagingServiceBlockingStub(
      MessagingServiceProperties properties) {
    ManagedChannel channel =
        ManagedChannelBuilder.forAddress(properties.url(), properties.port())
            .usePlaintext()
            .keepAliveTime(5, TimeUnit.SECONDS)
            .executor(Executors.newFixedThreadPool(1))
            .build();

    return MessagingServiceGrpc.newBlockingStub(channel);
  }
}
