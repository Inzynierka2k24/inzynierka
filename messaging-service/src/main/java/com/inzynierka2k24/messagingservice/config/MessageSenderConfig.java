package com.inzynierka2k24.messagingservice.config;

import com.vonage.client.VonageClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageSenderConfig {

  @Bean
  public VonageClient vonageClient(VonageClientProperties properties) {
    return VonageClient.builder().apiKey(properties.key()).apiSecret(properties.secret()).build();
  }
}
