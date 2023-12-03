package com.inzynierka2k24.apiserver.model;

import com.inzynierka2k24.ExternalService;
import java.util.Optional;

public record ExternalOffer(Optional<Long> id, ExternalService serviceType, String externalLink)
    implements External {

  public ExternalOffer(int serviceType, String externalLink) {
    this(Optional.empty(), ExternalService.forNumber(serviceType), externalLink);
  }

  public ExternalOffer(long id, int serviceType, String externalLink) {
    this(Optional.of(id), ExternalService.forNumber(serviceType), externalLink);
  }

  @Override
  public ExternalService getServiceType() {
    return serviceType;
  }
}
