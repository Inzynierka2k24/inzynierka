package com.inzynierka2k24.apiserver.model;

import java.time.Instant;
import java.util.Optional;

public record Finance(
    Optional<Long> id, // optional??
    long userId,
    long apartmentId,
    long eventId, // todo add services to the db
    int eventType,
    int costSource,
    float price,
    Instant date,
    String details) {}
