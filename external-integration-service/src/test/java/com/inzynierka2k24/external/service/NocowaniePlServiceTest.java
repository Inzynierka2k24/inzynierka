package com.inzynierka2k24.external.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import com.inzynierka2k24.ExternalService;
import com.inzynierka2k24.ResponseStatus;
import com.inzynierka2k24.external.crawler.BrowserProvider;
import com.inzynierka2k24.external.model.Account;
import com.inzynierka2k24.external.model.ApartmentDetails;
import com.inzynierka2k24.external.model.Reservation;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Response;
import com.microsoft.playwright.options.AriaRole;
import java.time.Instant;
import org.junit.jupiter.api.Test;

class NocowaniePlServiceTest {

  private final BrowserProvider browserProvider = mock(BrowserProvider.class);
  private final NocowaniePlService service =
      new NocowaniePlService(
          browserProvider, new Account("test", "test", "test", ExternalService.NOCOWANIEPL));

  @Test
  void shouldPropagateReservation() {
    var start = Instant.parse("2023-11-27T00:00:00Z");
    var end = Instant.parse("2023-12-02T00:00:00Z");
    var reservation = new Reservation(start, end);
    var page = mock(Page.class);
    var locator = mock(Locator.class);
    var response = mock(Response.class);

    when(locator.and(any(Locator.class))).thenReturn(locator);
    doNothing().when(locator).fill(any());
    doNothing().when(locator).click();
    doNothing().when(locator).press(any());
    when(page.locator(any())).thenReturn(locator);
    when(page.navigate(any())).thenReturn(response);
    when(page.url()).thenReturn("url");
    when(browserProvider.createPage(any())).thenReturn(page);

    assertEquals(ResponseStatus.SUCCESS, service.propagateReservation(reservation));
  }

  @Test
  void shouldNotPropagateReservation() {
    var start = Instant.parse("2023-11-27T00:00:00Z");
    var end = Instant.parse("2023-12-02T00:00:00Z");
    var reservation = new Reservation(start, end);
    when(browserProvider.createPage(any())).thenThrow(RuntimeException.class);

    assertEquals(ResponseStatus.FAILED, service.propagateReservation(reservation));
  }

  @Test // Just some dummy test
  void shouldNotGetReservations() {
    var start = Instant.parse("2023-11-27T00:00:00Z");
    var end = Instant.parse("2023-12-02T00:00:00Z");
    var page = mock(Page.class);
    var locator = mock(Locator.class);
    var response = mock(Response.class);

    when(locator.and(any(Locator.class))).thenReturn(locator);
    doNothing().when(locator).fill(any());
    doNothing().when(locator).click();
    doNothing().when(locator).press(any());
    when(page.locator(any())).thenReturn(locator);
    when(page.navigate(any())).thenReturn(response);
    when(page.url()).thenReturn("url");
    when(browserProvider.createPage(any())).thenReturn(page);

    var reservations = service.getReservations(start, end);
    assertThat(reservations).isEmpty();
  }

  @Test // Just some dummy test
  void shouldNotUpdateApartmentDetails() {
    var apartmentDetails = new ApartmentDetails("Apartment", "City", "Street", "1A", "Description");
    var page = mock(Page.class);
    var locator = mock(Locator.class);
    var response = mock(Response.class);

    when(locator.and(any(Locator.class))).thenReturn(locator);
    when(locator.getByRole(AriaRole.BUTTON)).thenReturn(locator);
    doNothing().when(locator).fill(any());
    doNothing().when(locator).click();
    doNothing().when(locator).press(any());
    when(page.locator(any())).thenReturn(locator);
    when(page.navigate(any())).thenReturn(response);
    when(page.url()).thenReturn("url");
    when(browserProvider.createPage(any())).thenReturn(page);

    assertEquals(ResponseStatus.FAILED, service.updateApartmentDetails(apartmentDetails));
  }

  @Test
  void shouldGetServiceType() {
    assertEquals(ExternalService.NOCOWANIEPL, service.getServiceType());
  }
}
