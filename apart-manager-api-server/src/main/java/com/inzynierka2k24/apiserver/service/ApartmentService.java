package com.inzynierka2k24.apiserver.service;

import com.inzynierka2k24.apiserver.dao.ApartmentDao;
import com.inzynierka2k24.apiserver.model.Apartment;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApartmentService {
  private final ApartmentDao apartmentDao;

  public List<Apartment> getAll() {
    return apartmentDao.getAll();
  }

  public void add(Apartment apartment) {
    apartmentDao.add(apartment);
  }

  public Apartment getById(long id) {
    return apartmentDao.getById(id).orElse(null);
  }

  public void update(Apartment apartment) {
    apartmentDao.update(apartment);
  }

  public void deleteById(long id) {
    apartmentDao.deleteById(id);
  }
}
