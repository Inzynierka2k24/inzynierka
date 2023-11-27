package com.inzynierka2k24.external.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.inzynierka2k24.ExternalService;
import com.inzynierka2k24.ResponseStatus;
import com.inzynierka2k24.external.crawler.Crawler;
import com.inzynierka2k24.external.model.Reservation;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import org.junit.jupiter.api.Test;

class NocowaniePlServiceTest {

  private final Crawler crawler = new Crawler();
  private final NocowaniePlService service = new NocowaniePlService(crawler);

  @Test
  void shouldPropagateReservation() {
    var start = Instant.parse("2023-11-27T00:00:00Z");
    var end = Instant.parse("2023-12-02T00:00:00Z");
    var reservation = new Reservation(start, end);

    assertEquals(ResponseStatus.SUCCESS, service.propagateReservation(reservation));
  }

  @Test
  void shouldGetReservations() {
    var start = Instant.parse("2023-11-29T00:00:00Z");
    System.out.println(start.truncatedTo(ChronoUnit.DAYS).toString().split("T")[0]);
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
