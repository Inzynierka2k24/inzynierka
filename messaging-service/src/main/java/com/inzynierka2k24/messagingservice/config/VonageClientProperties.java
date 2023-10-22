package com.inzynierka2k24.messagingservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "sms.client")
public record VonageClientProperties(String key, String secret) {}
