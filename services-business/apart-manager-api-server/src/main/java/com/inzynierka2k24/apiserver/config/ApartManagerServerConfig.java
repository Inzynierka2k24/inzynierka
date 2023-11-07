package com.inzynierka2k24.apiserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ApartManagerServerConfig {
  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }
}
