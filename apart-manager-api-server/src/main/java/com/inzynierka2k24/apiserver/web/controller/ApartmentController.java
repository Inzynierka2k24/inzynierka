package com.inzynierka2k24.apiserver.web.controller;

import com.inzynierka2k24.apiserver.model.Apartment;
import com.inzynierka2k24.apiserver.service.ApartmentService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ApartmentController {
  private final ApartmentService apartmentService;

  @GetMapping("/apartments/{userId}")
  public List<Apartment> getAll(@PathVariable long userId) {
    return apartmentService.getAll(userId);
  }

  @GetMapping("/apartment/{userId}/get/{apartmentId}")
  public Apartment get(@PathVariable long userId, @PathVariable long apartmentId) {
    return apartmentService.getById(userId, apartmentId).orElse(null);
  }

  @PostMapping("/apartment/{userId}/add")
  public void add(@PathVariable long userId, @RequestBody Apartment apartment) {
    apartmentService.add(userId, apartment);
  }

  @PutMapping("/apartment/{userId}/edit/{apartmentId}")
  public void edit(@PathVariable long userId, @RequestBody Apartment apartment) {
    apartmentService.update(userId, apartment);
  }

  @DeleteMapping("/apartment/{userId}/remove/{apartmentId}")
  public void delete(@PathVariable long userId, @PathVariable long apartmentId) {
    apartmentService.deleteById(userId, apartmentId);
  }
}
