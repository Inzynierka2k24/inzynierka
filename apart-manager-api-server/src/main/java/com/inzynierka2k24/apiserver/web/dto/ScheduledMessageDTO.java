package com.inzynierka2k24.apiserver.web.dto;

import com.inzynierka2k24.apiserver.model.Apartment;
import com.inzynierka2k24.apiserver.model.IntervalType;
import com.inzynierka2k24.apiserver.model.TriggerType;
import java.util.List;
import java.util.Optional;

public record ScheduledMessageDTO(
    Optional<Long> id,
    List<Apartment> apartments,
    String message,
    ContactDTO contact,
    IntervalType intervalType,
    int intervalValue,
    TriggerType triggerType) {}
