package com.inzynierka2k24.apiserver.web.controller;

import com.inzynierka2k24.apiserver.exception.offers.OfferNotFoundException;
import com.inzynierka2k24.apiserver.exception.offers.OfferNotValidException;
import com.inzynierka2k24.apiserver.model.ExternalOffer;
import com.inzynierka2k24.apiserver.service.ExternalOfferService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/apartment/{apartmentId}/external/offer")
@RequiredArgsConstructor
public class ExternalOfferController {

  private final ExternalOfferService offerService;

  @GetMapping()
  public ResponseEntity<List<ExternalOffer>> getAll(@PathVariable long apartmentId) {
    return ResponseEntity.ok(offerService.getAll(apartmentId));
  }

  @GetMapping("/{offerId}")
  public ResponseEntity<ExternalOffer> get(
      @PathVariable long apartmentId, @PathVariable long offerId) throws OfferNotFoundException {
    return ResponseEntity.ok(offerService.getById(apartmentId, offerId));
  }

  @PostMapping()
  public ResponseEntity<String> add(
      @PathVariable long apartmentId, @RequestBody ExternalOffer offer)
      throws OfferNotValidException {
    offerService.add(apartmentId, offer);
    return ResponseEntity.status(HttpStatus.CREATED).body("External offer created successfully");
  }

  @PutMapping()
  public ResponseEntity<String> edit(
      @PathVariable long apartmentId, @RequestBody ExternalOffer offer)
      throws OfferNotValidException {
    offerService.update(apartmentId, offer);
    return ResponseEntity.ok("Reservation updated successfully");
  }

  @DeleteMapping("/{offerId}")
  public ResponseEntity<String> delete(@PathVariable long apartmentId, @PathVariable long offerId) {
    offerService.deleteById(apartmentId, offerId);
    return ResponseEntity.ok("Reservation deleted successfully");
  }
}
