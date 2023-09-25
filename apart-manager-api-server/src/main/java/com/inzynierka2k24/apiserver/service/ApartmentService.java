package com.inzynierka2k24.apiserver.service;

import com.inzynierka2k24.apiserver.dao.ApartmentDao;
import com.inzynierka2k24.apiserver.model.Apartment;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApartmentService {
  private final ApartmentDao apartmentDao;

  public List<Apartment> getAll(long userId) {
    return apartmentDao.getAll(userId);
  }

  public Optional<Apartment> getById(long userId, long apartmentId) {
    return apartmentDao.getById(userId, apartmentId);
  }

  public void add(long userId, Apartment apartment) {
    apartmentDao.add(userId, apartment);
  }

  public void update(long userId, Apartment apartment) {
    if (apartment.id().isPresent()) {
      apartmentDao.update(userId, apartment);
    }
  }

  public void deleteById(long userId, long apartmentId) {
    apartmentDao.deleteById(userId, apartmentId);
  }
}
