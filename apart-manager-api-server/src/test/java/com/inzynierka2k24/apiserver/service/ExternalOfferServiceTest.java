package com.inzynierka2k24.apiserver.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.inzynierka2k24.ExternalService;
import com.inzynierka2k24.apiserver.dao.ExternalOfferDao;
import com.inzynierka2k24.apiserver.exception.offers.OfferNotFoundException;
import com.inzynierka2k24.apiserver.exception.offers.OfferNotValidException;
import com.inzynierka2k24.apiserver.model.ExternalOffer;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class ExternalOfferServiceTest {

  private final ExternalOfferDao offerDao = mock(ExternalOfferDao.class);
  private final ExternalOfferService service = new ExternalOfferService(offerDao);

  @Test
  void shouldGetAllExternalOffersForApartmentId() {
    // Given
    long apartmentId = 1;
    List<ExternalOffer> expectedOffers =
        List.of(new ExternalOffer(1, 1, "link1"), new ExternalOffer(2, 1, "link2"));
    when(offerDao.getAll(apartmentId)).thenReturn(expectedOffers);

    // When
    List<ExternalOffer> actualOffers = service.getAll(apartmentId);

    // Then
    assertEquals(expectedOffers, actualOffers);
  }

  @Test
  void shouldgetExternalOfferByIdForApartmentId() throws OfferNotFoundException {
    // Given
    long apartmentId = 1;
    long offerId = 1;
    ExternalOfferDao offerDao = mock(ExternalOfferDao.class);
    ExternalOffer expectedOffer = new ExternalOffer(1, 1, "link1");
    when(offerDao.getById(apartmentId, offerId)).thenReturn(Optional.of(expectedOffer));
    ExternalOfferService service = new ExternalOfferService(offerDao);

    // When
    ExternalOffer actualOffer = service.getById(apartmentId, offerId);

    // Then
    assertEquals(expectedOffer, actualOffer);
  }

  @Test
  void shouldaddNewExternalOfferForApartmentIdAndServiceType() throws OfferNotValidException {
    // Given
    long apartmentId = 1;
    ExternalOffer offer = new ExternalOffer(1, "link1");
    ExternalOfferDao offerDao = mock(ExternalOfferDao.class);
    when(offerDao.getByServiceType(apartmentId, offer.serviceType())).thenReturn(Optional.empty());
    ExternalOfferService service = new ExternalOfferService(offerDao);

    // When
    service.add(apartmentId, offer);

    // Then
    verify(offerDao).add(apartmentId, offer);
  }

  @Test
  public void shouldupdateExistingExternalOfferForApartmentIdAndServiceType()
      throws OfferNotValidException {
    // Given
    long apartmentId = 1;
    ExternalOffer offer = new ExternalOffer(1, 1, "link1");
    ExternalOfferDao offerDao = mock(ExternalOfferDao.class);
    Optional<ExternalOffer> offerToUpdate = Optional.of(new ExternalOffer(1, 1, "link2"));
    when(offerDao.getByServiceType(apartmentId, offer.serviceType())).thenReturn(offerToUpdate);
    ExternalOfferService service = new ExternalOfferService(offerDao);

    // When
    service.update(apartmentId, offer);

    // Then
    verify(offerDao).update(offer);
  }

  @Test
  public void shouldDeleteExternalOfferByIdForApartmentId() {
    // Given
    long apartmentId = 1;
    long offerId = 1;
    ExternalOfferDao offerDao = mock(ExternalOfferDao.class);
    ExternalOfferService service = new ExternalOfferService(offerDao);

    // When
    service.deleteById(apartmentId, offerId);

    // Then
    verify(offerDao).deleteById(apartmentId, offerId);
  }

  @Test
  public void shouldThrowOfferNotFoundExceptionWhenGettingNonExistingExternalOffer() {
    // Given
    long apartmentId = 1;
    long offerId = 1;
    ExternalOfferDao offerDao = mock(ExternalOfferDao.class);
    when(offerDao.getById(apartmentId, offerId)).thenReturn(Optional.empty());
    ExternalOfferService service = new ExternalOfferService(offerDao);

    // When/Then
    assertThrows(OfferNotFoundException.class, () -> service.getById(apartmentId, offerId));
  }

  @Test
  public void shouldThrowOfferNotValidExceptionWhenAddingOrUpdatingExistingOffer() {
    // Given
    long apartmentId = 1;
    String externalLink = "link1";
    ExternalOffer existingOffer = new ExternalOffer(1, 1, externalLink);
    ExternalOffer newOffer = new ExternalOffer(2, 1, externalLink);
    when(offerDao.getByServiceType(apartmentId, ExternalService.AIRBNB))
        .thenReturn(Optional.of(existingOffer));

    // When, Then
    assertThrows(OfferNotValidException.class, () -> service.add(apartmentId, newOffer));
    assertThrows(OfferNotValidException.class, () -> service.update(apartmentId, newOffer));
  }
}
