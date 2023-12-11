package com.inzynierka2k24.apiserver.dao;

import com.inzynierka2k24.apiserver.model.EventType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class FinanceSourceDaoTest {

  private FinanceSourceDao financeSourceDao;

  @BeforeEach
  void setUp() {
    financeSourceDao = new FinanceSourceDao();
  }

  @Test
  void getAllShouldReturnCorrectMappings() {
    Map<String, List<String>> mappings = financeSourceDao.getAll();

    for (EventType eventType : EventType.values()) {
      List<String> expectedSources = eventType.getValidSources().stream().map(Enum::name).toList();
      List<String> actualSources = mappings.get(eventType.name());

      assertNotNull(actualSources, "Mapping for " + eventType.name() + " should not be null");
      assertEquals(expectedSources, actualSources, "Mapping for " + eventType.name() + " does not match expected sources");
    }
  }
}
