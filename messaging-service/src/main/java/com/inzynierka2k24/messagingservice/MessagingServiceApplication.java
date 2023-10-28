package com.inzynierka2k24.messagingservice;

import com.inzynierka2k24.messagingservice.config.VonageClientProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableConfigurationProperties(VonageClientProperties.class)
@EnableMongoRepositories
public class MessagingServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(MessagingServiceApplication.class, args);
  }
}
