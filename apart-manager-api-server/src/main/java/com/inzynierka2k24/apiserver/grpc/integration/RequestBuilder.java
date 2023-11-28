package com.inzynierka2k24.apiserver.grpc.integration;

import static com.inzynierka2k24.apiserver.grpc.util.TimeConverter.toProtoTimestamp;

import com.inzynierka2k24.*;
import com.inzynierka2k24.apiserver.model.Apartment;
import com.inzynierka2k24.apiserver.model.ExternalAccount;
import com.inzynierka2k24.apiserver.model.Reservation;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collection;

public final class RequestBuilder {

  public static PropagateReservationRequest buildPropagateReservationRequest(
      Reservation reservation, Collection<ExternalAccount> accounts) {
    return PropagateReservationRequest.newBuilder()
        .setReservation(toProto(reservation))
        .addAllAccounts(accounts.stream().map(RequestBuilder::toProto).toList())
        .build();
  }

  public static GetReservationsRequest buildGetReservationsRequest(
      Instant from, Instant to, Collection<ExternalAccount> accounts) {
    return GetReservationsRequest.newBuilder()
        .setFrom(toProtoTimestamp(from))
        .setTo(toProtoTimestamp(to))
        .addAllAccounts(accounts.stream().map(RequestBuilder::toProto).toList())
        .build();
  }

  public static UpdateApartmentDetailsRequest buildUpdateApartmentDetailsRequest(
      Apartment apartment, Collection<ExternalAccount> accounts) {
    return UpdateApartmentDetailsRequest.newBuilder()
        .setDetails(toProto(apartment))
        .addAllAccounts(accounts.stream().map(RequestBuilder::toProto).toList())
        .build();
  }

  public static com.inzynierka2k24.Reservation toProto(Reservation reservation) {
    return com.inzynierka2k24.Reservation.newBuilder()
        .setStartDate(toProtoTimestamp(Instant.now().plus(2L, ChronoUnit.DAYS)))
        .setEndDate(toProtoTimestamp(Instant.now().plus(5L, ChronoUnit.DAYS)))
        .build();
  }

  public static com.inzynierka2k24.ExternalAccount toProto(ExternalAccount account) {
    return com.inzynierka2k24.ExternalAccount.newBuilder()
        .setAccount(
            Account.newBuilder().setLogin(account.login()).setPassword(account.password()).build())
        .setService(account.serviceType())
        .build();
  }

  public static ApartmentDetails toProto(Apartment apartment) {
    return ApartmentDetails.newBuilder()
        .setTitle(apartment.title())
        .setCity(apartment.city())
        .setStreet(apartment.street())
        .setBuildingNumber(apartment.buildingNumber())
        .setDescription(
            String.join(
                ", ",
                apartment.title(),
                apartment.country(),
                apartment.city(),
                apartment.street(),
                apartment.apartmentNumber()))
        .build();
  }
}
