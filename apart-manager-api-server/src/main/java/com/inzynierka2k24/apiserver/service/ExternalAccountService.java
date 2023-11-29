package com.inzynierka2k24.apiserver.service;

import com.inzynierka2k24.apiserver.dao.ExternalAccountDao;
import com.inzynierka2k24.apiserver.exception.account.AccountNotFoundException;
import com.inzynierka2k24.apiserver.model.ExternalAccount;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExternalAccountService {

  private final ExternalAccountDao accountDao;

  public List<ExternalAccount> getAll(long userId) {
    return accountDao.getAll(userId);
  }

  public ExternalAccount getById(long userId, long accountId) throws AccountNotFoundException {
    return accountDao.getById(userId, accountId).orElseThrow(AccountNotFoundException::new);
  }

  public void add(long userId, ExternalAccount account) {
    accountDao.add(userId, account);
  }

  public void update(long userId, ExternalAccount account) throws AccountNotFoundException {
    if (account.id().isPresent() && existsById(userId, account.id().get())) {
      accountDao.update(userId, account);
    } else {
      throw new AccountNotFoundException();
    }
  }

  public void deleteById(long userId, long accountId) throws AccountNotFoundException {
    if (existsById(userId, accountId)) {
      accountDao.deleteById(userId, accountId);
    } else {
      throw new AccountNotFoundException();
    }
  }

  boolean existsById(long userId, long accountId) {
    return accountDao.getById(userId, accountId).isPresent();
  }
}
