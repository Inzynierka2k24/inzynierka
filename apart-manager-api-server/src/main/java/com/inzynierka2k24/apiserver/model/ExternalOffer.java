package com.inzynierka2k24.apiserver.model;

import java.util.Optional;

public record ExternalOffer(Optional<Long> id, ServiceType serviceType, String externalLink) {}
