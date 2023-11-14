package com.inzynierka2k24.apiserver.service;

import com.inzynierka2k24.apiserver.dao.UserDao;
import com.inzynierka2k24.apiserver.exception.user.UserAlreadyExistsException;
import com.inzynierka2k24.apiserver.exception.user.UserNotFoundException;
import com.inzynierka2k24.apiserver.model.User;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserDao userDao;

  public User getUser(String username) {
    Optional<User> user = userDao.get(username);

    if (user.isEmpty()) {
      throw new UsernameNotFoundException("Not found : " + username);
    }

    return user.get();
  }

  public void register(User user) throws UserAlreadyExistsException {
    if (!existsByMail(user.emailAddress())) {
      userDao.register(user);
    } else {
      throw new UserAlreadyExistsException();
    }
  }

  public void update(User user) throws UserNotFoundException {
    if (existsByMail(user.emailAddress())) {
      userDao.update(user);
    } else {
      throw new UserNotFoundException();
    }
  }

  public void deleteById(long userId) throws UserNotFoundException {
    if (existsById(userId)) {
      userDao.deleteById(userId);
    } else {
      throw new UserNotFoundException();
    }
  }

  boolean existsByMail(String emailAddress) {
    return userDao.getByEmail(emailAddress).isPresent();
  }

  boolean existsById(long userId) {
    return userDao.get(userId).isPresent();
  }
}
