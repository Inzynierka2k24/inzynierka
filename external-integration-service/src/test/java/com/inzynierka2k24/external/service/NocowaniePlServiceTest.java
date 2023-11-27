package com.inzynierka2k24.external.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import com.inzynierka2k24.ExternalService;
import com.inzynierka2k24.ResponseStatus;
import com.inzynierka2k24.external.crawler.Crawler;
import com.inzynierka2k24.external.model.Account;
import org.junit.jupiter.api.Test;

class NocowaniePlServiceTest {

  //  private final Crawler crawler = new Crawler();
  private final Crawler crawler = mock(Crawler.class);

  private final NocowaniePlService service =
      new NocowaniePlService(
          crawler,
          new Account("apartmanager404@gmail.com", "inzynierka2k24", ExternalService.NOCOWANIEPL));

  //  @Test
  //  void shouldPropagateReservation() {
  //    var start = Instant.parse("2023-11-27T00:00:00Z");
  //    var end = Instant.parse("2023-12-02T00:00:00Z");
  //    var reservation = new Reservation(start, end);
  //    when(crawler.createPage(any())).thenReturn(mock(Page.class));
  //
  //    assertEquals(ResponseStatus.SUCCESS, service.propagateReservation(reservation));
  //  }

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
