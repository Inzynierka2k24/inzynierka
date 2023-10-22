package com.inzynierka2k24.messagingservice.model;

import com.inzynierka2k24.EventType;
import com.inzynierka2k24.Status;
import java.time.Instant;
import lombok.With;

public record MessageDetails(
    long eventId, EventType eventType, Instant date, @With Status messageStatus) {}
