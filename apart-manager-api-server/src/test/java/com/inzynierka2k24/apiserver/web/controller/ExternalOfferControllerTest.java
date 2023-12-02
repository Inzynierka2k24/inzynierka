package com.inzynierka2k24.apiserver.web.controller;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.inzynierka2k24.apiserver.exception.offers.OfferNotFoundException;
import com.inzynierka2k24.apiserver.exception.offers.OfferNotValidException;
import com.inzynierka2k24.apiserver.model.ExternalOffer;
import com.inzynierka2k24.apiserver.service.ExternalOfferService;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class ExternalOfferControllerTest {

  private final ExternalOfferService offerService = mock(ExternalOfferService.class);
  private final ExternalOfferController offerController = new ExternalOfferController(offerService);

  @Test
  void shouldGetAllReturnsListOfExternalOffers() {
    // Given
    long apartmentId = 1;
    List<ExternalOffer> expectedOffers = new ArrayList<>();
    expectedOffers.add(new ExternalOffer(1, 1, "externalLink1"));
    expectedOffers.add(new ExternalOffer(2, 2, "externalLink2"));
    when(offerService.getAll(apartmentId)).thenReturn(expectedOffers);

    // When
    ResponseEntity<List<ExternalOffer>> response = offerController.getAll(apartmentId);

    // Then
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(expectedOffers, response.getBody());
  }

  @Test
  void shouldGetByIdReturnsSingleExternalOffer() throws OfferNotFoundException {
    // Given
    long apartmentId = 1;
    long offerId = 1;
    ExternalOffer expectedOffer = new ExternalOffer(1, 1, "externalLink");
    when(offerService.getById(apartmentId, offerId)).thenReturn(expectedOffer);

    // When
    ResponseEntity<ExternalOffer> response = offerController.get(apartmentId, offerId);

    // Then
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(expectedOffer, response.getBody());
  }

  @Test
  void shouldAddCreatesNewExternalOffer() throws OfferNotValidException {
    // Given
    long apartmentId = 1;
    ExternalOffer offer = new ExternalOffer(1, 1, "externalLink");

    // When
    ResponseEntity<String> response = offerController.add(apartmentId, offer);

    // Then
    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertEquals("External offer created successfully", response.getBody());
    verify(offerService).add(apartmentId, offer);
  }

  @Test
  void shouldUpdateExistingExternalOffer() throws OfferNotValidException {
    // Given
    long apartmentId = 1;
    ExternalOffer offer = new ExternalOffer(1, 1, "externalLink");

    // When
    ResponseEntity<String> response = offerController.edit(apartmentId, offer);

    // Then
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Reservation updated successfully", response.getBody());
    verify(offerService).update(apartmentId, offer);
  }

  @Test
  void shouldDeleteDeletesExistingExternalOffer() {
    // Given
    long apartmentId = 1;
    long offerId = 1;

    // When
    ResponseEntity<String> response = offerController.delete(apartmentId, offerId);

    // Then
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Reservation deleted successfully", response.getBody());
    verify(offerService).deleteById(apartmentId, offerId);
  }

  @Test
  void shouldAddWithExistingServiceTypeReturns400Error() throws OfferNotValidException {
    // Given
    long apartmentId = 1;
    ExternalOffer offer = new ExternalOffer(1, 1, "externalLink");
    doThrow(OfferNotValidException.class)
        .when(offerService)
        .add(any(long.class), any(ExternalOffer.class));

    // When, Then
    assertThatThrownBy(() -> offerController.add(apartmentId, offer))
        .isInstanceOf(OfferNotValidException.class);
  }

  @Test
  void shouldEditWithExistingServiceTypeReturns400Error() throws OfferNotValidException {
    // Given
    long apartmentId = 1;
    ExternalOffer offer = new ExternalOffer(1, 1, "externalLink");
    doThrow(OfferNotValidException.class)
        .when(offerService)
        .update(any(long.class), any(ExternalOffer.class));

    // When, Then
    assertThatThrownBy(() -> offerController.edit(apartmentId, offer))
        .isInstanceOf(OfferNotValidException.class);
  }
}
