package com.inzynierka2k24.external.service;

import com.inzynierka2k24.ResponseStatus;
import com.inzynierka2k24.external.crawler.BrowserProvider;
import com.inzynierka2k24.external.model.Account;
import com.inzynierka2k24.external.model.ApartmentDetails;
import com.inzynierka2k24.external.model.Reservation;
import com.microsoft.playwright.Page;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
class NocowaniePlService implements ExternalService {

  private static final String URL = "https://admin.noclegi.pl/";

  private final BrowserProvider browserProvider;
  private final Account account;

  @Override
  public ResponseStatus propagateReservation(Reservation reservation) {
    try {
      var date = reservation.start();
      var end = reservation.end().plus(1, ChronoUnit.DAYS);
      var page = browserProvider.createPage(URL);
      logIn(page);
      page.navigate(page.url().replace("reservations", "pricetable"));

      while (date.isBefore(end)) {
        var shortDate = convertToShortDateString(date);
        log.info("Changing availability for {}", shortDate);
        changeToUnavailable(page, shortDate);
        date = date.plus(1, ChronoUnit.DAYS);
      }

      return ResponseStatus.SUCCESS;
    } catch (Exception e) {
      return ResponseStatus.FAILED;
    }
  }

  @Override
  public Set<Reservation> getReservations() {
    return Set.of();
  }

  @Override
  public ResponseStatus updateApartmentDetails(ApartmentDetails details) {
    return ResponseStatus.FAILED;
  }

  @Override
  public com.inzynierka2k24.ExternalService getServiceType() {
    return com.inzynierka2k24.ExternalService.NOCOWANIEPL;
  }

  private void logIn(Page page) {
    page.locator("[name='email']").fill(account.login());
    page.locator("[name='password']").fill(account.password());
    page.locator("[type='submit']").click();
  }

  private void changeToUnavailable(Page page, String date) {
    var element =
        page.locator(String.format("[data-date='%s']", date))
            .and(page.locator("[data-available-room-count]"));
    element.fill("0");
    element.press("Enter");
  }

  private String convertToShortDateString(Instant date) {
    return date.toString().split("T")[0];
  }
}
