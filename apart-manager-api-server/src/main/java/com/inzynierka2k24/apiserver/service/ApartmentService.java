package com.inzynierka2k24.apiserver.service;

import com.inzynierka2k24.apiserver.dao.ApartmentDao;
import com.inzynierka2k24.apiserver.exception.apartment.ApartmentNotFoundException;
import com.inzynierka2k24.apiserver.model.Apartment;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApartmentService {

  private final ApartmentDao apartmentDao;

  public List<Apartment> getAll(long userId) {
    return apartmentDao.getAll(userId);
  }

  public Apartment getById(long userId, long apartmentId) throws ApartmentNotFoundException {
    return apartmentDao.getById(userId, apartmentId).orElseThrow(ApartmentNotFoundException::new);
  }

  public void add(long userId, Apartment apartment) {
    apartmentDao.add(userId, apartment);
  }

  public void update(long userId, Apartment apartment) throws ApartmentNotFoundException {
    if (apartment.id().isPresent() && existsById(userId, apartment.id().get())) {
      apartmentDao.update(userId, apartment);
    } else {
      throw new ApartmentNotFoundException();
    }
  }

  public void deleteById(long userId, long apartmentId) throws ApartmentNotFoundException {
    if (existsById(userId, apartmentId)) {
      apartmentDao.deleteById(userId, apartmentId);
    } else {
      throw new ApartmentNotFoundException();
    }
  }

  boolean existsById(long userId, long apartmentId) {
    return apartmentDao.getById(userId, apartmentId).isPresent();
  }
}
