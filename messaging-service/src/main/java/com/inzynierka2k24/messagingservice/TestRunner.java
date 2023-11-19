package com.inzynierka2k24.messagingservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class TestRunner implements ApplicationRunner {

  private final String mailUsername;
  private final String mailPassword;
  private final String dbUser;
  private final String dbPassword;
  private final String dbUrl;
  private final String clientKey;
  private final String clientSecret;

  public TestRunner(
      @Value("${spring.mail.username}") String mailUsername,
      @Value("${spring.mail.password}") String mailPassword,
      @Value("${spring.data.mongodb.username}") String dbUser,
      @Value("${spring.data.mongodb.password}") String dbPassword,
      @Value("${spring.data.mongodb.host}") String dbUrl,
      @Value("${sms.client.key}") String clientKey,
      @Value("${sms.client.secret}") String clientSecret) {
    this.mailUsername = mailUsername;
    this.mailPassword = mailPassword;
    this.dbUser = dbUser;
    this.dbPassword = dbPassword;
    this.dbUrl = dbUrl;
    this.clientKey = clientKey;
    this.clientSecret = clientSecret;
  }

  @Override
  public void run(ApplicationArguments args) throws Exception {
     System.out.println(String.join("#", mailUsername, System.getenv("MAIL_USER")));
     System.out.println(String.join("#", mailPassword, System.getenv("MAIL_PASSWORD")));
     System.out.println(String.join("#", dbUser, System.getenv("MESSAGE_DB_USER")));
     System.out.println(String.join("#", dbPassword, System.getenv("MESSAGE_DB_PASSWORD")));
     System.out.println(String.join("#", dbUrl, System.getenv("MESSAGE_DB_URL")));
     System.out.println(String.join("#", clientKey, System.getenv("SMS_CLIENT_KEY")));
     System.out.println(String.join("#", clientSecret, System.getenv("SMS_CLIENT_SECRET")));
  }
}
