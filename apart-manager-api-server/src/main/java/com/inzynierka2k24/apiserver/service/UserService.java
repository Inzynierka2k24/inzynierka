package com.inzynierka2k24.apiserver.service;

import com.inzynierka2k24.apiserver.dao.UserDao;
import com.inzynierka2k24.apiserver.model.User;
import com.inzynierka2k24.apiserver.model.UserSecurityDetails;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
  private final UserDao userDao;

  private final PasswordEncoder passwordEncoder;

  public Optional<User> get(String mail, String password) {
    return userDao.get(mail, password);
  }

  public void register(String mail, String password) {
    if (userDao.get(mail, password).isEmpty()) {
      userDao.register(new User(mail, passwordEncoder.encode(password)));
    }
  }

  public void update(long userId, String password, String mail) {
    userDao.update(new User(userId, mail, password));
  }

  public void deleteById(long userId) {
    userDao.deleteById(userId);
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<User> user = userDao.get(username);

    if (user.isEmpty()) {
      throw new UsernameNotFoundException("Not found : " + username);
    }

    return new UserSecurityDetails(user.get());
  }
}
