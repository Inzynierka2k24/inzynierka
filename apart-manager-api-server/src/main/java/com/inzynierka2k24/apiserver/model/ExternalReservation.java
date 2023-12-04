package com.inzynierka2k24.apiserver.model;

import com.inzynierka2k24.ExternalService;
import java.util.Optional;

public record ExternalReservation(
    Reservation reservation, Optional<Float> price, ExternalService serviceType) {}
