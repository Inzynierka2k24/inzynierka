package com.inzynierka2k24.apiserver.service;

import com.inzynierka2k24.apiserver.dao.FinanceDao;
import com.inzynierka2k24.apiserver.exception.finance.FinanceNotFoundException;
import com.inzynierka2k24.apiserver.model.Finance;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FinanceService {

  private final FinanceDao financeDao;

  public List<Finance> getAll(long userId) {
    return financeDao.getAll(userId);
  }

  public Finance getById(long financeId) throws FinanceNotFoundException {
    return financeDao.getById(financeId).orElseThrow(FinanceNotFoundException::new);
  }

  public List<Finance> getByApartmentId(long apartmentId) {
    return financeDao.getByApartmentId(apartmentId);
  }

  public void add(Finance finance) {
    financeDao.add(finance);
  }

  public void update(Finance finance) throws FinanceNotFoundException {
    if (finance.id().isPresent() && existsById(finance.id().get())) {
      financeDao.update(finance);
    } else {
      throw new FinanceNotFoundException();
    }
  }

  public void deleteById(long financeId) throws FinanceNotFoundException {
    if (existsById(financeId)) {
      financeDao.deleteById(financeId);
    } else {
      throw new FinanceNotFoundException();
    }
  }

  boolean existsById(long financeId) {
    return financeDao.getById(financeId).isPresent();
  }
}
