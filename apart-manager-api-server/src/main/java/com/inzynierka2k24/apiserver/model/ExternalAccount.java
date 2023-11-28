package com.inzynierka2k24.apiserver.model;

import com.inzynierka2k24.ExternalService;
import java.util.Optional;

public record ExternalAccount(
    Optional<Long> id, String login, String password, ExternalService serviceType) {}
