package com.inzynierka2k24.external.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.inzynierka2k24.ExternalService;
import com.inzynierka2k24.ResponseStatus;
import org.junit.jupiter.api.Test;

// TODO Test it properly when methods will be implemented
class NocowaniePlServiceTest {

  private final NocowaniePlService service = new NocowaniePlService();

  @Test
  void shouldPropagateReservation() {
    assertEquals(ResponseStatus.FAILED, service.propagateReservation(null));
  }

  @Test
  void shouldGetReservations() {
    assertThat(service.getReservations()).isEmpty();
  }

  @Test
  void shouldUpdateApartmentDetails() {
    assertEquals(ResponseStatus.FAILED, service.updateApartmentDetails(null));
  }

  @Test
  void shouldGetServiceType() {
    assertEquals(ExternalService.NOCOWANIEPL, service.getServiceType());
  }
}
