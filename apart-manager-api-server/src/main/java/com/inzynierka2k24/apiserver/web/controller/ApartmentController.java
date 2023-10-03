package com.inzynierka2k24.apiserver.web.controller;

import com.inzynierka2k24.apiserver.model.Apartment;
import com.inzynierka2k24.apiserver.service.ApartmentService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/{userId}/apartment")
@RequiredArgsConstructor
public class ApartmentController {
  private final ApartmentService apartmentService;

  @GetMapping()
  public List<Apartment> getAll(@PathVariable long userId) {
    return apartmentService.getAll(userId);
  }

  @GetMapping("/{apartmentId}")
  public Apartment get(@PathVariable long userId, @PathVariable long apartmentId) {
    return apartmentService.getById(userId, apartmentId).orElse(null);
  }

  @PostMapping()
  public void add(@PathVariable long userId, @RequestBody Apartment apartment) {
    apartmentService.add(userId, apartment);
  }

  @PutMapping()
  public void edit(@PathVariable long userId, @RequestBody Apartment apartment) {
    apartmentService.update(userId, apartment);
  }

  @DeleteMapping("/{apartmentId}")
  public void delete(@PathVariable long userId, @PathVariable long apartmentId) {
    apartmentService.deleteById(userId, apartmentId);
  }
}
