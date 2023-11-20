package com.inzynierka2k24.apiserver.model;

import java.util.Optional;

public record Contact(
    Optional<Long> id, Optional<Long> userId, ContactType contactType, String name) {}
