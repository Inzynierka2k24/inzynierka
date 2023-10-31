package com.inzynierka2k24.apiserver.model;

import java.time.Instant;
import java.util.Optional;

public record Finance(
    Optional<Long> id,
    long userId,
    long apartmentId,
    long eventId,
    int eventType,
    int costSource,
    float price,
    Instant date,
    String details) {}
