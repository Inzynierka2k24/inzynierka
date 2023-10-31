package com.inzynierka2k24.apiserver.web.controller;

import com.inzynierka2k24.apiserver.exception.apartment.ApartmentNotFoundException;
import com.inzynierka2k24.apiserver.model.Apartment;
import com.inzynierka2k24.apiserver.service.ApartmentService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/{userId}/apartment")
@RequiredArgsConstructor
public class ApartmentController {
  private final ApartmentService apartmentService;

  @GetMapping()
  public ResponseEntity<List<Apartment>> getAll(@PathVariable long userId) {
    return ResponseEntity.ok(apartmentService.getAll(userId));
  }

  @GetMapping("/{apartmentId}")
  public ResponseEntity<Apartment> get(@PathVariable long userId, @PathVariable long apartmentId)
      throws ApartmentNotFoundException {
    return ResponseEntity.ok(apartmentService.getById(userId, apartmentId));
  }

  @PostMapping()
  public ResponseEntity<String> add(@PathVariable long userId, @RequestBody Apartment apartment) {
    apartmentService.add(userId, apartment);
    return ResponseEntity.status(HttpStatus.CREATED).body("Apartment created successfully");
  }

  @PutMapping()
  public ResponseEntity<String> edit(@PathVariable long userId, @RequestBody Apartment apartment)
      throws ApartmentNotFoundException {
    apartmentService.update(userId, apartment);
    return ResponseEntity.ok("Apartment edited successfully");
  }

  @DeleteMapping("/{apartmentId}")
  public ResponseEntity<String> delete(@PathVariable long userId, @PathVariable long apartmentId)
      throws ApartmentNotFoundException {
    apartmentService.deleteById(userId, apartmentId);
    return ResponseEntity.ok("Apartment deleted successfully");
  }
}
