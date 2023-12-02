package com.inzynierka2k24.external.server.request;

import static com.inzynierka2k24.external.util.TimeConverter.toInstant;

import com.inzynierka2k24.ExternalAccount;
import com.inzynierka2k24.external.model.Account;
import com.inzynierka2k24.external.model.ApartmentDetails;
import com.inzynierka2k24.external.model.Reservation;
import java.util.Collection;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RequestConverter {

  public static Reservation convert(com.inzynierka2k24.Reservation request) {
    return new Reservation(toInstant(request.getStartDate()), toInstant(request.getEndDate()));
  }

  public static Collection<Account> convert(Collection<ExternalAccount> request) {
    return request.stream().map(RequestConverter::convert).toList();
  }

  public static Account convert(ExternalAccount request) {
    var account = request.getAccount();
    return new Account(
        account.getLogin(), account.getPassword(), account.getLink(), request.getService());
  }

  public static ApartmentDetails convert(com.inzynierka2k24.ApartmentDetails request) {
    return new ApartmentDetails(
        request.getTitle(),
        request.getCity(),
        request.getStreet(),
        request.getBuildingNumber(),
        request.getDescription());
  }
}
