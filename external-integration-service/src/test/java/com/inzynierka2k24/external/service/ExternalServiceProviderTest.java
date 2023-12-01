package com.inzynierka2k24.external.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import com.inzynierka2k24.ExternalService;
import com.inzynierka2k24.external.crawler.BrowserProvider;
import com.inzynierka2k24.external.model.Account;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ExternalServiceProviderTest {

  private final ExternalServiceProvider provider =
      new ExternalServiceProvider(
          mock(BrowserProvider.class),
          mock(BookingService.class),
          mock(AirbnbService.class),
          mock(TrivagoService.class));

  @ParameterizedTest
  @MethodSource
  void shouldGetMessageSender(
      Account account,
      Class<? extends com.inzynierka2k24.external.service.ExternalService> expectedClass) {
    assertEquals(
        expectedClass, provider.getServices(List.of(account)).findAny().orElseThrow().getClass());
  }

  @Test
  void shouldReturnNullWhenUnrecognizedType() {
    assertThrows(
        IllegalArgumentException.class,
        () ->
            provider
                .getServices(List.of(new Account("", "", "", ExternalService.UNRECOGNIZED)))
                .findAny());
  }

  static Stream<Arguments> shouldGetMessageSender() {
    return Stream.of(
        Arguments.of(new Account("", "", "", ExternalService.BOOKING), BookingService.class),
        Arguments.of(new Account("", "", "", ExternalService.AIRBNB), AirbnbService.class),
        Arguments.of(new Account("", "", "", ExternalService.TRIVAGO), TrivagoService.class),
        Arguments.of(
            new Account("", "", "", ExternalService.NOCOWANIEPL), NocowaniePlService.class));
  }
}
