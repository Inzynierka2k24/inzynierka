package com.inzynierka2k24.apiserver.web.controller;

import com.inzynierka2k24.Reservation;
import com.inzynierka2k24.apiserver.exception.apartment.ApartmentNotFoundException;
import com.inzynierka2k24.apiserver.exception.reservation.ReservationNotFoundException;
import com.inzynierka2k24.apiserver.service.IntegrationService;
import com.inzynierka2k24.apiserver.web.request.GetReservationsRequest;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/{userId}/external/integration")
@RequiredArgsConstructor
public class IntegrationController {

  private final IntegrationService integrationService;

  @GetMapping("/propagate/apartment/{apartmentId}/reservation/{reservationId}")
  public ResponseEntity<Map<String, String>> propagateReservation(
      @PathVariable long userId, @PathVariable long apartmentId, @PathVariable long reservationId)
      throws ReservationNotFoundException {
    return ResponseEntity.ok(
        integrationService.propagateReservation(userId, apartmentId, reservationId));
  }

  @PostMapping("/apartment/{apartmentId}/get/reservations")
  public ResponseEntity<List<Reservation>> getReservations(
      @PathVariable long userId,
      @PathVariable long apartmentId,
      @RequestBody GetReservationsRequest request) {
    return ResponseEntity.ok(
        integrationService.getReservations(userId, apartmentId, request.from(), request.to()));
  }

  @GetMapping("/update/apartment/{apartmentId}")
  public ResponseEntity<Map<String, String>> updateApartmentDetails(
      @PathVariable long userId, @PathVariable long apartmentId) throws ApartmentNotFoundException {
    return ResponseEntity.ok(integrationService.updateApartmentDetails(userId, apartmentId));
  }
}
