package com.inzynierka2k24.apiserver.web.dto;

import java.time.Instant;

public record FinanceDTO(
    Long id,
    long userId,
    long apartmentId,
    String eventType,
    String source,
    float price,
    Instant date,
    String details) {}
