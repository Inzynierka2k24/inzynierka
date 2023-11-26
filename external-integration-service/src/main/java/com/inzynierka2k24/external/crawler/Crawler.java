package com.inzynierka2k24.external.crawler;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

// @Component
public class Crawler {

  private final Playwright playwright;
  private final Browser browser;

  public Crawler() {
    try {
      playwright = Playwright.create();
      browser =
          playwright
              .firefox()
              .launch(new BrowserType.LaunchOptions().setHeadless(false).setSlowMo(50));
    } catch (Exception e) {
      throw new RuntimeException("Unable to start Crawler", e);
    }
  }

  public Page createPage(String url) {
    var page = browser.newPage();
    page.navigate(url);
    return page;
  }
}
