package com.inzynierka2k24.apiserver.model;

import java.util.Optional;

public record ExternalAccount(
    Optional<Long> id, String login, String password, String mail, ServiceType serviceType) {}
