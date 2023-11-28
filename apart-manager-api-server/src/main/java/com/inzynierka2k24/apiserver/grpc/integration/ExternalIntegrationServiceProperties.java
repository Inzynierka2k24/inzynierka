package com.inzynierka2k24.apiserver.grpc.integration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("grpc.external-integration-service")
public record ExternalIntegrationServiceProperties(String url, int port) {}
