package com.inzynierka2k24.apiserver.web.controller;

import com.inzynierka2k24.apiserver.exception.finance.FinanceNotFoundException;
import com.inzynierka2k24.apiserver.model.Finance;
import com.inzynierka2k24.apiserver.service.FinanceService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/{userId}/finance")
@RequiredArgsConstructor
public class FinanceController {
  private final FinanceService financeService;

  @GetMapping()
  public ResponseEntity<List<Finance>> getAll(@PathVariable long userId) {
    return ResponseEntity.ok(financeService.getAll(userId));
  }

  @GetMapping("/{financeId}")
  public ResponseEntity<Finance> get(@PathVariable long financeId) throws FinanceNotFoundException {
    return ResponseEntity.ok(financeService.getById(financeId));
  }

  @GetMapping("/apartment/{apartmentId}")
  public ResponseEntity<List<Finance>> getAllByApartmentId(@PathVariable long apartmentId) {
    List<Finance> finances = financeService.getByApartmentId(apartmentId);
    return ResponseEntity.ok(finances);
  }

  @PostMapping()
  public ResponseEntity<String> add(@RequestBody Finance finance) {
    financeService.add(finance);
    return ResponseEntity.status(HttpStatus.CREATED).body("Finance created successfully");
  }

  @PutMapping("/{financeId}")
  public ResponseEntity<String> edit(@RequestBody Finance finance) {
    try {
      financeService.update(finance);
      return ResponseEntity.ok("Finance edited successfully");
    } catch (FinanceNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Finance not found");
    }
  }

  @DeleteMapping("/{financeId}")
  public ResponseEntity<String> delete(@PathVariable long financeId) {
    try {
      financeService.deleteById(financeId);
      return ResponseEntity.ok("Finance deleted successfully");
    } catch (FinanceNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Finance not found");
    }
  }
}
