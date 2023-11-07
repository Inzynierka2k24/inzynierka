package com.inzynierka2k24.messagingservice.repository;

import com.inzynierka2k24.EventType;
import com.inzynierka2k24.messagingservice.model.Message;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MessageRepository extends MongoRepository<Message, String> {

  List<Message> findByContent_ReceiverAndDetails_EventIdAndDetails_EventType(
      String receiver, long eventId, EventType eventType);
}
