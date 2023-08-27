package com.inzynierka2k24.apiserver.model;

import java.time.Instant;

public record Reservation(long id, Instant startDate, Instant endDate) {}
