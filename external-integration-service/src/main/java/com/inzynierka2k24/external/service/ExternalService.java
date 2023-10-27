package com.inzynierka2k24.external.service;

import com.inzynierka2k24.ResponseStatus;
import com.inzynierka2k24.external.model.ApartmentDetails;
import com.inzynierka2k24.external.model.Reservation;
import java.util.Set;

interface ExternalService {

  ResponseStatus propagateReservation(Reservation reservation);

  Set<Reservation> getReservations();

  ResponseStatus updateApartmentDetails(ApartmentDetails details);
}
