package com.inzynierka2k24.apiserver.grpc.messaging;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "grpc.messaging-service")
public record MessagingServiceProperties(String url, int port) {}
