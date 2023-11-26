package com.inzynierka2k24.external.service;

import com.inzynierka2k24.ResponseStatus;
import com.inzynierka2k24.external.crawler.Crawler;
import com.inzynierka2k24.external.model.ApartmentDetails;
import com.inzynierka2k24.external.model.Reservation;
import com.microsoft.playwright.Page;
import java.time.Instant;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class NocowaniePlService implements ExternalService {

  private static final String URL = "https://admin.noclegi.pl/";

  private final Crawler crawler;

  @Override
  public ResponseStatus propagateReservation(Reservation reservation) {
    var page = crawler.createPage(URL);
    logIn(page, "apartmanager404@gmail.com", "inzynierka2k24");
    page.navigate(page.url().replace("reservations", "pricetable"));

    try {
      var date = reservation.start();

      while (date.isBefore(reservation.end())) {
        System.out.println(convertToString(date));
        changeToUnavailable(page, convertToString(date));
        date.plus(1, ChronoUnit.DAYS);
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

  private void logIn(Page page, String mail, String password) {
    page.locator("[name='email']").fill(mail);
    page.locator("[name='password']").fill(password);
    page.locator("[type='submit']").click();
  }

  private void changeToUnavailable(Page page, String date) {
    var element =
        page.locator(String.format("[data-date='%s']", date))
            .and(page.locator("[data-available-room-count]"));
    element.fill("0");
    element.press("Enter");
  }

  private String convertToString(Instant date) {
    return String.join(
        "-",
        String.valueOf(date.get(ChronoField.YEAR)),
        String.valueOf(date.get(ChronoField.MONTH_OF_YEAR)),
        String.valueOf(date.get(ChronoField.DAY_OF_MONTH)));
  }
}
