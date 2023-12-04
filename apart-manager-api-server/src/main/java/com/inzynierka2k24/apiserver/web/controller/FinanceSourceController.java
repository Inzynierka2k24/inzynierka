package com.inzynierka2k24.apiserver.web.controller;

import com.inzynierka2k24.apiserver.service.FinanceSourceService;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/finance-source")
@RequiredArgsConstructor
public class FinanceSourceController {

  private final FinanceSourceService financeSourceService;

  @GetMapping
  public Map<String, List<String>> getEventsWithSources() {
    return financeSourceService.getEventsWithSources();
  }
}
