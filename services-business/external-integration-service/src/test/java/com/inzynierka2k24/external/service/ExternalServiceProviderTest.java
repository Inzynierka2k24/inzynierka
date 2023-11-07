package com.inzynierka2k24.external.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import com.inzynierka2k24.ExternalService;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ExternalServiceProviderTest {

  private final ExternalServiceProvider provider =
      new ExternalServiceProvider(
          mock(BookingService.class),
          mock(AirbnbService.class),
          mock(TrivagoService.class),
          mock(NocowaniePlService.class));

  @ParameterizedTest
  @MethodSource
  void shouldGetMessageSender(
      ExternalService externalService,
      Class<? extends com.inzynierka2k24.external.service.ExternalService> expectedClass) {
    assertEquals(
        expectedClass,
        provider.getServices(List.of(externalService)).findAny().orElseThrow().getClass());
  }

  @Test
  void shouldReturnNullWhenUnrecognizedType() {
    assertThrows(
        IllegalArgumentException.class,
        () -> provider.getServices(List.of(ExternalService.UNRECOGNIZED)).findAny());
  }

  static Stream<Arguments> shouldGetMessageSender() {
    return Stream.of(
        Arguments.of(ExternalService.BOOKING, BookingService.class),
        Arguments.of(ExternalService.AIRBNB, AirbnbService.class),
        Arguments.of(ExternalService.TRIVAGO, TrivagoService.class),
        Arguments.of(ExternalService.NOCOWANIEPL, NocowaniePlService.class));
  }
}
