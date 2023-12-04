package com.inzynierka2k24.apiserver.dao;

import com.inzynierka2k24.apiserver.model.EventType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

// TODO: This probably shouldn't be hardcoded
@Repository
public class FinanceSourceDao {

  private static final Map<String, List<String>> eventToSources = new HashMap<>();

  static {
    for (EventType eventType : EventType.values()) {
      List<String> sources = eventType.getValidSources().stream()
          .map(Enum::name)
          .collect(Collectors.toList());
      eventToSources.put(eventType.name(), sources);
    }
  }
  public Map<String, List<String>> getAll() {
    return eventToSources;
  }
}
