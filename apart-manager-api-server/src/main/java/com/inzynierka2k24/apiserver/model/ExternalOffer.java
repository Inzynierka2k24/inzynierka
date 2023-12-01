package com.inzynierka2k24.apiserver.model;

import com.inzynierka2k24.ExternalService;
import java.util.Optional;

public record ExternalOffer(Optional<Long> id, ExternalService serviceType, String externalLink) {

  public ExternalOffer(int serviceType, String externalLink) {
    this(Optional.empty(), ExternalService.forNumber(serviceType), externalLink);
  }
}
