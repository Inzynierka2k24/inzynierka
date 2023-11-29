package com.inzynierka2k24.apiserver.model;

import java.util.Optional;

public record Apartment(
    Optional<Long> id,
    float dailyPrice,
    String title,
    String country,
    String city,
    String street,
    String buildingNumber,
    String apartmentNumber,
    int rating) {}
