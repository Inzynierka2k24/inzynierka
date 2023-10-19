package com.inzynierka2k24.messagingservice.service;

import com.inzynierka2k24.messagingservice.model.MessageStatus;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageStatusService {

  public Set<MessageStatus> get(String receiver) {
    return Set.of();
  }

  public void save(MessageStatus messageStatus) {
    // TODO Save status in Mongo
  }
}
