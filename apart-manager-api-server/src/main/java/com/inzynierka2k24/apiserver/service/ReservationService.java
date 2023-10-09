package com.inzynierka2k24.apiserver.service;

import com.inzynierka2k24.apiserver.dao.ReservationDao;
import com.inzynierka2k24.apiserver.model.Reservation;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservationService {
  private final ReservationDao reservationDao;

  public List<Reservation> getAll(long userId) {
    return reservationDao.getAll(userId);
  }

  public Optional<Reservation> getById(long apartmentId, long reservationId) {
    return reservationDao.getById(apartmentId, reservationId);
  }

  public void add(long apartmentId, Reservation reservation) {
    if (isValid(reservation)) {
      reservationDao.add(apartmentId, reservation);
    }
  }

  public void update(long apartmentId, Reservation reservation) {
    if (isValid(reservation) && reservation.id().isPresent()) {
      reservationDao.update(apartmentId, reservation);
    }
  }

  public void deleteById(long apartmentId, long reservationId) {
    reservationDao.deleteById(apartmentId, reservationId);
  }

  private boolean isValid(Reservation reservation) {
    return reservation.startDate().isBefore(reservation.endDate());
  }
}
