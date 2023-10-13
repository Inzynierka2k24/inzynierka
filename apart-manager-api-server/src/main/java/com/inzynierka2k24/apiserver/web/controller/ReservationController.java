package com.inzynierka2k24.apiserver.web.controller;

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
    return reservationService.getById(apartmentId, reservationId).orElse(null);
  }

  @PostMapping()
  public void add(@PathVariable long apartmentId, @RequestBody Reservation reservation) {
    reservationService.add(apartmentId, reservation);
  }

  @PutMapping()
  public void edit(@PathVariable long apartmentId, @RequestBody Reservation reservation) {
    reservationService.update(reservation);
  }

  @DeleteMapping("/{reservationId}")
  public void delete(@PathVariable long apartmentId, @PathVariable long reservationId) {
    reservationService.deleteById(apartmentId, reservationId);
  }
}
