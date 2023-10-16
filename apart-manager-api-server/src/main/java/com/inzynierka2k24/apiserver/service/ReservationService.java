package com.inzynierka2k24.apiserver.service;

import com.inzynierka2k24.apiserver.dao.ReservationDao;
import com.inzynierka2k24.apiserver.exception.reservation.ReservationNotFoundException;
import com.inzynierka2k24.apiserver.exception.reservation.ReservationNotValidException;
import com.inzynierka2k24.apiserver.model.Reservation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservationService {

  private final ReservationDao reservationDao;

  public List<Reservation> getAll(long apartmentId) {
    return reservationDao.getAll(apartmentId);
  }

  public Reservation getById(long apartmentId, long reservationId)
      throws ReservationNotFoundException {
    return reservationDao
        .getById(apartmentId, reservationId)
        .orElseThrow(ReservationNotFoundException::new);
  }

  public void add(long apartmentId, Reservation reservation) throws ReservationNotValidException {
    if (reservationDao.isTimePeriodFree(reservation)) {
      reservationDao.add(apartmentId, reservation);
    } else {
      throw new ReservationNotValidException("Chosen time period is already taken.");
    }
  }

  public void update(Reservation reservation) throws ReservationNotValidException {
    if (reservation.id().isPresent() && reservationDao.isTimePeriodFree(reservation)) {
      reservationDao.update(reservation);
    } else {
      throw new ReservationNotValidException("Chosen time period is already taken.");
    }
  }

  public void deleteById(long apartmentId, long reservationId) {
    reservationDao.deleteById(apartmentId, reservationId);
  }
}
