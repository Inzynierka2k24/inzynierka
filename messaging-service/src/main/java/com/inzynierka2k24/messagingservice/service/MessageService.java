package com.inzynierka2k24.messagingservice.service;

import com.inzynierka2k24.EventType;
import com.inzynierka2k24.Status;
import com.inzynierka2k24.messagingservice.model.Message;
import com.inzynierka2k24.messagingservice.model.MessageDetails;
import com.inzynierka2k24.messagingservice.repository.MessageRepository;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageService {

  private final MessageRepository repository;

  public Set<MessageDetails> get(String receiver, long eventId, EventType eventType) {
    return repository
        .findByContent_ReceiverAndDetails_EventIdAndDetails_EventType(receiver, eventId, eventType)
        .stream()
        .map(Message::details)
        .collect(Collectors.toSet());
  }

  public Status save(Message message) {
    try {
      repository.save(message);
    } catch (Exception e) {
      return Status.FAILED;
    }

    return Status.STORED;
  }
}
