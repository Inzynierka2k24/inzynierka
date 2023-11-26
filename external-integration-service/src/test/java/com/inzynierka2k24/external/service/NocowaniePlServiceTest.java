package com.inzynierka2k24.external.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.inzynierka2k24.ExternalService;
import com.inzynierka2k24.ResponseStatus;
import com.inzynierka2k24.external.crawler.Crawler;
import com.inzynierka2k24.external.model.Reservation;
import java.time.Instant;
import org.junit.jupiter.api.Test;

class NocowaniePlServiceTest {

  private final Crawler crawler = new Crawler();
  private final NocowaniePlService service = new NocowaniePlService(crawler);

  @Test
  void shouldPropagateReservation() {
    var start = Instant.parse("2023-11-26T00:00:00Z");
    var end = Instant.parse("2023-12-02T00:00:00Z");
    var reservation = new Reservation(start, end);

    assertEquals(ResponseStatus.SUCCESS, service.propagateReservation(reservation));
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
