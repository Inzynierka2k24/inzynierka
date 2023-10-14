package com.inzynierka2k24.apiserver.service;

import com.inzynierka2k24.apiserver.dao.UserDao;
import com.inzynierka2k24.apiserver.exception.user.UserAlreadyExistsException;
import com.inzynierka2k24.apiserver.exception.user.UserNotFoundException;
import com.inzynierka2k24.apiserver.model.User;
import com.inzynierka2k24.apiserver.model.UserSecurityDetails;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

  private final UserDao userDao;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<User> user = userDao.get(username);

    if (user.isEmpty()) {
      throw new UsernameNotFoundException("Not found : " + username);
    }

    return new UserSecurityDetails(user.get());
  }

  public void register(User user) throws UserAlreadyExistsException {
    if (!existsByMail(user.mail())) {
      userDao.register(user);
    } else {
      throw new UserAlreadyExistsException();
    }
  }

  public void update(User user) throws UserNotFoundException {
    if (existsByMail(user.mail())) {
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

  boolean existsByMail(String mail) {
    return userDao.get(mail).isPresent();
  }

  boolean existsById(long userId) {
    return userDao.get(userId).isPresent();
  }
}
