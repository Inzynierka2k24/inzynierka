package com.inzynierka2k24.messagingservice.repository;

import static org.assertj.core.api.Assertions.*;

import com.inzynierka2k24.EventType;
import com.inzynierka2k24.MessageType;
import com.inzynierka2k24.Status;
import com.inzynierka2k24.messagingservice.model.Message;
import com.inzynierka2k24.messagingservice.model.MessageContent;
import com.inzynierka2k24.messagingservice.model.MessageDetails;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.annotation.DirtiesContext;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@DirtiesContext
@Testcontainers
public class MessageRepositoryTest {

  @Container @ServiceConnection
  static MongoDBContainer container = new MongoDBContainer("mongo:latest");

  @Autowired MessageRepository repository;

  @BeforeEach
  public void setUp() {
    repository.deleteAll();

    var content = new MessageContent("test@example.com", "Test Subject", "Test Content");
    var details = new MessageDetails(1L, EventType.RESERVATION, Instant.EPOCH, Status.STORED);
    repository.save(new Message(content, details, MessageType.MAIL));
  }

  @Test
  public void findsByReceiverAndEventIdAndEventType() {
    List<Message> result =
        repository.findByContent_ReceiverAndDetails_EventIdAndDetails_EventType(
            "test@example.com", 1L, EventType.RESERVATION);

    assertThat(result).hasSize(1).extracting("content.receiver").contains("test@example.com");
  }
}
