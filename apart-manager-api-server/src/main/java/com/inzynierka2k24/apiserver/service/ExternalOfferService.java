package com.inzynierka2k24.apiserver.service;

import com.inzynierka2k24.apiserver.dao.ExternalOfferDao;
import com.inzynierka2k24.apiserver.exception.offers.OfferNotFoundException;
import com.inzynierka2k24.apiserver.exception.offers.OfferNotValidException;
import com.inzynierka2k24.apiserver.model.ExternalOffer;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExternalOfferService {

  private final ExternalOfferDao offerDao;

  public List<ExternalOffer> getAll(long apartmentId) {
    return offerDao.getAll(apartmentId);
  }

  public ExternalOffer getById(long apartmentId, long offerId) throws OfferNotFoundException {
    return offerDao.getById(apartmentId, offerId).orElseThrow(OfferNotFoundException::new);
  }

  public void add(long apartmentId, ExternalOffer offer) throws OfferNotValidException {
    if (offerDao.getByServiceType(apartmentId, offer.serviceType()).isEmpty()) {
      offerDao.add(apartmentId, offer);
    } else {
      throw new OfferNotValidException("External offer for given service already exists.");
    }
  }

  public void update(long apartmentId, ExternalOffer offer) throws OfferNotValidException {
    var offerToUpdate = offerDao.getByServiceType(apartmentId, offer.serviceType());
    if (offerToUpdate.isEmpty() || hasSameId(offer, offerToUpdate.get())) {
      offerDao.update(offer);
    } else {
      throw new OfferNotValidException("External offer for given service already exists.");
    }
  }

  public void deleteById(long apartmentId, long offerId) {
    offerDao.deleteById(apartmentId, offerId);
  }

  private boolean hasSameId(ExternalOffer newOffer, ExternalOffer oldOffer) {
    return oldOffer.id().isPresent()
        && newOffer.id().isPresent()
        && oldOffer.id().get().equals(newOffer.id().get());
  }
}
