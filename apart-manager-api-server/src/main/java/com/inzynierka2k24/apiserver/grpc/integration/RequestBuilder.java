package com.inzynierka2k24.apiserver.grpc.integration;

import static com.inzynierka2k24.apiserver.grpc.util.TimeConverter.toProtoTimestamp;

import com.inzynierka2k24.*;
import com.inzynierka2k24.apiserver.model.*;
import com.inzynierka2k24.apiserver.model.ExternalAccount;
import com.inzynierka2k24.apiserver.model.Reservation;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class RequestBuilder {

  public static PropagateReservationRequest buildPropagateReservationRequest(
      Reservation reservation,
      Collection<ExternalAccount> accounts,
      Collection<ExternalOffer> offers) {
    return PropagateReservationRequest.newBuilder()
        .setReservation(toProto(reservation))
        .addAllAccounts(buildExternalAccounts(accounts, offers))
        .build();
  }

  public static GetReservationsRequest buildGetReservationsRequest(
      Instant from,
      Instant to,
      Collection<ExternalAccount> accounts,
      Collection<ExternalOffer> offers) {
    return GetReservationsRequest.newBuilder()
        .setFrom(toProtoTimestamp(from))
        .setTo(toProtoTimestamp(to))
        .addAllAccounts(buildExternalAccounts(accounts, offers))
        .build();
  }

  public static UpdateApartmentDetailsRequest buildUpdateApartmentDetailsRequest(
      Apartment apartment, Collection<ExternalAccount> accounts, Collection<ExternalOffer> offers) {
    return UpdateApartmentDetailsRequest.newBuilder()
        .setDetails(toProto(apartment))
        .addAllAccounts(buildExternalAccounts(accounts, offers))
        .build();
  }

  public static com.inzynierka2k24.Reservation toProto(Reservation reservation) {
    return com.inzynierka2k24.Reservation.newBuilder()
        .setStartDate(toProtoTimestamp(reservation.startDate()))
        .setEndDate(toProtoTimestamp(reservation.endDate()))
        .build();
  }

  public static com.inzynierka2k24.ExternalAccount toProto(
      ExternalAccount account, ExternalOffer offer) {
    return com.inzynierka2k24.ExternalAccount.newBuilder()
        .setAccount(
            Account.newBuilder()
                .setLogin(account.login())
                .setPassword(account.password())
                .setLink(offer.externalLink())
                .build())
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

  static Collection<com.inzynierka2k24.ExternalAccount> buildExternalAccounts(
      Collection<ExternalAccount> accounts, Collection<ExternalOffer> offers) {
    var offersMap = groupByServiceType(offers);
    return groupByServiceType(accounts).entrySet().stream()
        .map(entry -> toProto(entry.getValue(), offersMap.get(entry.getKey())))
        .toList();
  }

  private static <T extends External> Map<ExternalService, T> groupByServiceType(
      Collection<T> externals) {
    return externals.stream()
        .collect(Collectors.toMap(External::getServiceType, Function.identity()));
  }
}
