package com.inzynierka2k24.external.crawler;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Disabled
class BrowserProviderTest {

  private final BrowserProvider provider = new BrowserProvider();

  @Test
  void shouldCreatePage() {
    assertDoesNotThrow(() -> provider.createPage("https://web.usos.pwr.edu.pl/"));
  }
}
