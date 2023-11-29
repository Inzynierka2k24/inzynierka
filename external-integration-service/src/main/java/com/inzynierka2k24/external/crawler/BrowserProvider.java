package com.inzynierka2k24.external.crawler;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.springframework.stereotype.Component;

@Component
public class BrowserProvider {

  private final Playwright playwright;
  private final Browser browser;

  public BrowserProvider() {
    try {
      playwright = Playwright.create();
      browser = playwright.firefox().launch();
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
