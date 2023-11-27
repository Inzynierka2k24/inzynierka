package com.inzynierka2k24.external.service;

import com.inzynierka2k24.external.crawler.Crawler;
import com.inzynierka2k24.external.model.Account;
import java.util.Collection;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class ExternalServiceProvider {

  private final Crawler crawler;

  private final BookingService bookingService;
  private final AirbnbService airbnbService;
  private final TrivagoService trivagoService;

  Stream<? extends ExternalService> getServices(Collection<Account> accounts) {
    return accounts.stream()
        .map(
            account ->
                switch (account.service()) {
                  case BOOKING -> bookingService;
                  case AIRBNB -> airbnbService;
                  case TRIVAGO -> trivagoService;
                  case NOCOWANIEPL -> new NocowaniePlService(crawler, account);
                  case UNRECOGNIZED -> throw new IllegalArgumentException("Unrecognized service!");
                });
  }
}
