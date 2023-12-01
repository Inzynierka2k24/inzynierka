package com.inzynierka2k24.apiserver.web.dto;

import com.inzynierka2k24.apiserver.model.Apartment;
import java.time.Instant;
import java.util.Optional;

public record ReservationDTO(
    Optional<Long> id, Apartment apartment, Instant startDate, Instant endDate) {}
