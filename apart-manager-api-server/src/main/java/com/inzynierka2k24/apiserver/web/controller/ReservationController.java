package com.inzynierka2k24.apiserver.web.controller;

import com.inzynierka2k24.apiserver.exception.reservation.ReservationNotFoundException;
import com.inzynierka2k24.apiserver.exception.reservation.ReservationNotValidException;
import com.inzynierka2k24.apiserver.model.Reservation;
import com.inzynierka2k24.apiserver.service.ReservationService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/{userId}/apartment/{apartmentId}/reservation")
@RequiredArgsConstructor
public class ReservationController {

  private final ReservationService reservationService;

  private static final String NOT_VALID_RESERVATION_CAUSE = "Start date needs to be after end date";

  @GetMapping()
  public ResponseEntity<List<Reservation>> getAll(@PathVariable long apartmentId) {
    return ResponseEntity.ok(reservationService.getAll(apartmentId));
  }

  @GetMapping("/{reservationId}")
  public ResponseEntity<Reservation> get(
      @PathVariable long apartmentId, @PathVariable long reservationId)
      throws ReservationNotFoundException {
    return ResponseEntity.ok(reservationService.getById(apartmentId, reservationId));
  }

  @PostMapping()
  public ResponseEntity<String> add(
      @PathVariable long apartmentId, @RequestBody Reservation reservation)
      throws ReservationNotValidException {
    if (isNotValid(reservation)) {
      throw new ReservationNotValidException(NOT_VALID_RESERVATION_CAUSE);
    }

    reservationService.add(apartmentId, reservation);
    return ResponseEntity.status(HttpStatus.CREATED).body("Reservation created successfully");
  }

  @PutMapping()
  public ResponseEntity<String> edit(@RequestBody Reservation reservation)
      throws ReservationNotValidException {
    if (isNotValid(reservation)) {
      throw new ReservationNotValidException(NOT_VALID_RESERVATION_CAUSE);
    }

    reservationService.update(reservation);
    return ResponseEntity.ok("Reservation updated successfully");
  }

  @DeleteMapping("/{reservationId}")
  public ResponseEntity<String> delete(
      @PathVariable long apartmentId, @PathVariable long reservationId) {
    reservationService.deleteById(apartmentId, reservationId);
    return ResponseEntity.ok("Reservation deleted successfully");
  }

  boolean isNotValid(Reservation reservation) {
    return reservation.startDate().isAfter(reservation.endDate());
  }
}
