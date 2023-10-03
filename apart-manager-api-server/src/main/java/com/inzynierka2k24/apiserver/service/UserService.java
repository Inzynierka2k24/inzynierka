package com.inzynierka2k24.apiserver.service;

import com.inzynierka2k24.apiserver.dao.UserDao;
import com.inzynierka2k24.apiserver.model.User;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
  private final UserDao userDao;

  public Optional<User> get(String login, String password) {
    return userDao.get(login, password);
  }

  public void register(String login, String password, String mail) {
    userDao.register(new User(login, password, mail));
  }

  public void update(long userId, String password, String mail) {
    userDao.update(new User(userId, password, mail));
  }

  public void deleteById(long userId) {
    userDao.deleteById(userId);
  }
}
