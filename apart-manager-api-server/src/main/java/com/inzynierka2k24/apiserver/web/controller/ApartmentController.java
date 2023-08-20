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

  @GetMapping("/apartments")
  public List<Apartment> getAll() {
    return apartmentService.getAll();
  }

  @PostMapping("/apartment/add")
  public void add(@ModelAttribute("apartment") Apartment apartment) {
    apartmentService.add(apartment);
  }
}
