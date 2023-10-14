package com.inzynierka2k24.apiserver.web.controller;

import com.inzynierka2k24.apiserver.exception.reservation.ReservationNotFoundException;
import com.inzynierka2k24.apiserver.exception.reservation.ReservationNotValidException;
import com.inzynierka2k24.apiserver.model.Reservation;
import com.inzynierka2k24.apiserver.service.ReservationService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/{userId}/apartment/{apartmentId}/reservation")
@RequiredArgsConstructor
public class ReservationController {

  private final ReservationService reservationService;

  @GetMapping()
  public List<Reservation> getAll(@PathVariable long apartmentId) {
    return reservationService.getAll(apartmentId);
  }

  @GetMapping("/{reservationId}")
  public Reservation get(@PathVariable long apartmentId, @PathVariable long reservationId) {
    try {
      return reservationService.getById(apartmentId, reservationId);
    } catch (ReservationNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  @PostMapping()
  public void add(@PathVariable long apartmentId, @RequestBody Reservation reservation) {
    if (isNotValid(reservation)) {
      return;
    }

    try {
      reservationService.add(apartmentId, reservation);
    } catch (ReservationNotValidException e) {
      throw new RuntimeException(e);
    }
  }

  @PutMapping()
  public void edit(@RequestBody Reservation reservation) {
    if (isNotValid(reservation)) {
      return;
    }

    try {
      reservationService.update(reservation);
    } catch (ReservationNotValidException e) {
      throw new RuntimeException(e);
    }
  }

  @DeleteMapping("/{reservationId}")
  public void delete(@PathVariable long apartmentId, @PathVariable long reservationId) {
    reservationService.deleteById(apartmentId, reservationId);
  }

  boolean isNotValid(Reservation reservation) {
    return reservation.startDate().isAfter(reservation.endDate());
  }
}
