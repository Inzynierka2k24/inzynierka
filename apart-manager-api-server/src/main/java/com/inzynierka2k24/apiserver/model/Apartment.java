package com.inzynierka2k24.apiserver.model;

public record Apartment(
    long id,
    float dailyPrice,
    String title,
    String country,
    String city,
    String street,
    String buildingNumber,
    String apartmentNumber) {}
