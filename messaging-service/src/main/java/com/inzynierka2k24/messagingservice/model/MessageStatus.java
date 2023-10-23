package com.inzynierka2k24.messagingservice.model;

import com.inzynierka2k24.EventType;
import com.inzynierka2k24.Status;
import java.time.Instant;
import java.util.Optional;

public record MessageStatus(
    String receiver,
    Status messageStatus,
    Optional<Long> eventId,
    EventType eventType,
    Instant date) {}
