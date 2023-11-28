package com.inzynierka2k24.apiserver.web.request;

import java.time.Instant;

public record GetReservationsRequest(Instant from, Instant to) {}
