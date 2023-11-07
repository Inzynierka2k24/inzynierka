package com.inzynierka2k24.apiserver.model;

import java.util.Optional;

public record Contact(
    Optional<Long> id, ContactType contactType, String receiver, String message) {}
