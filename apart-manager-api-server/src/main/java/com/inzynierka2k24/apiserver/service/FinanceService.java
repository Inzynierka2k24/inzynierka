package com.inzynierka2k24.apiserver.service;

import com.inzynierka2k24.apiserver.dao.FinanceDao;
import com.inzynierka2k24.apiserver.exception.finance.FinanceNotFoundException;
import com.inzynierka2k24.apiserver.model.Finance;
import com.inzynierka2k24.apiserver.web.dto.FinanceDTO;
import java.util.List;
import java.util.Optional;
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

  public void add(FinanceDTO financeDto) {
    Finance finance = new Finance(
        Optional.empty(),
        financeDto.userId(),
        financeDto.apartmentId(),
        financeDto.eventType(),
        financeDto.source(),
        financeDto.price(),
        financeDto.date(),
        financeDto.details());
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
