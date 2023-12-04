package com.inzynierka2k24.external.service;

import static java.time.ZoneOffset.UTC;
import static java.time.temporal.ChronoUnit.DAYS;

import com.inzynierka2k24.ResponseStatus;
import com.inzynierka2k24.external.crawler.BrowserProvider;
import com.inzynierka2k24.external.model.Account;
import com.inzynierka2k24.external.model.ApartmentDetails;
import com.inzynierka2k24.external.model.Reservation;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
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
      page.navigate(String.join("/", URL, account.externalLink(), "pricetable"));

      while (date.isBefore(end)) {
        var shortDate = convertToShortDateString(date);
        log.info("Changing availability for {} for login {}", shortDate, account.login());
        changeToUnavailable(page, shortDate);
        date = date.plus(1, ChronoUnit.DAYS);
      }

      return ResponseStatus.SUCCESS;
    } catch (Exception e) {
      return ResponseStatus.FAILED;
    }
  }

  @Override
  public Set<Reservation> getReservations(Instant from, Instant to) {
    try {
      var date = from;
      var end = to.plus(1, ChronoUnit.DAYS);
      var page = browserProvider.createPage(URL);
      HashSet<Reservation> reservations = new HashSet<>();

      logIn(page);
      page.navigate(String.join("/", URL, account.externalLink(), "pricetable"));
      page.locator("[name='startDate']").click();

      // Pick right time period
      int clicksNum = getMonth(Instant.now()) - getMonth(from);
      click(page.locator("[class='glyphicon glyphicon-chevron-left']"), clicksNum);
      page.locator(String.format("[data-month='%s']", getMonth(from) - 1))
          .and(page.locator(String.format("[data-day='%s']", getDayOfMonth(from))))
          .click();

      clicksNum = getMonth(end) - getMonth(from);
      click(page.locator("[class='glyphicon glyphicon-chevron-right']"), clicksNum);
      page.locator(String.format("[data-month='%s']", getMonth(end) - 1))
          .and(page.locator(String.format("[data-day='%s']", getDayOfMonth(end))))
          .click();

      // Create reservation base on apartment availability
      Instant start = null;
      float price = 0;

      while (date.isBefore(end)) {
        var shortDate = convertToShortDateString(date);
        int reserved =
            Integer.parseInt(
                page.locator(String.format("[data-date='%s']", shortDate))
                    .and(page.locator("[data-available-room-count]"))
                    .getAttribute("value"));

        if (reserved == 0 && start == null) {
          start = date;
        }

        if (reserved == 0) {
          int dailyPrice =
              Integer.parseInt(
                  page.locator(String.format("[data-date='%s']", shortDate))
                      .and(page.locator("[data-unit-type='adult']"))
                      .getAttribute("value"));
          price += dailyPrice;
        }

        if (reserved != 0 && start != null) {
          reservations.add(
              new Reservation(
                  start,
                  date.minus(1, DAYS),
                  price,
                  com.inzynierka2k24.ExternalService.NOCOWANIEPL));
          start = null;
          price = 0;
        }

        date = date.plus(1, ChronoUnit.DAYS);
      }

      return reservations;
    } catch (Exception e) {
      return Set.of();
    }
  }

  @Override
  public ResponseStatus updateApartmentDetails(ApartmentDetails details) {
    try {
      var page = browserProvider.createPage(URL);

      logIn(page);
      page.navigate(String.join("/", URL, account.externalLink(), "description"));

      page.locator("[id='description_19']").fill(details.description());
      page.getByRole(AriaRole.BUTTON)
          .and(page.locator("[class='btn btn-lg btn-primary submit-btn']"))
          .click();

      return ResponseStatus.SUCCESS;
    } catch (Exception e) {
      return ResponseStatus.FAILED;
    }
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

  private int getMonth(Instant instant) {
    return instant.atOffset(UTC).getMonth().getValue();
  }

  private int getDayOfMonth(Instant instant) {
    return instant.atOffset(UTC).getDayOfMonth();
  }

  private void click(Locator locator, int times) {
    for (int i = 0; i < times; i++) {
      locator.click();
    }
  }
}
