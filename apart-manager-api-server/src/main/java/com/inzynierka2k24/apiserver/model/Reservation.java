package com.inzynierka2k24.apiserver.model;

import java.time.Instant;
import java.util.Optional;

public record Reservation(Optional<Long> id, Instant startDate, Instant endDate) {}
