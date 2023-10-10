package com.inzynierka2k24.apiserver.service;

import com.inzynierka2k24.apiserver.dao.UserDao;
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

  public void register(User user) {
    userDao.register(user);
  }

  public void update(User user) {
    userDao.update(user);
  }

  public void deleteById(long userId) {
    userDao.deleteById(userId);
  }

  public boolean existsByMail(String mail) {
    return userDao.get(mail).isPresent();
  }

  public boolean userIdMatchesEmail() {
    return true;
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
