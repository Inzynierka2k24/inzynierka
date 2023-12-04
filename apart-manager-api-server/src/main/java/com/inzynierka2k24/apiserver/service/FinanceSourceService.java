package com.inzynierka2k24.apiserver.service;

import com.inzynierka2k24.apiserver.dao.FinanceSourceDao;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FinanceSourceService {

  private final FinanceSourceDao financeSourceDao;

  public Map<String, List<String>> getEventsWithSources() {
    return financeSourceDao.getAll();
  }
}
