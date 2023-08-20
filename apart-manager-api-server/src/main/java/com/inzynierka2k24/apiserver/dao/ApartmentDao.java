package com.inzynierka2k24.apiserver.dao;

import com.inzynierka2k24.apiserver.model.Apartment;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApartmentDao {
  private final JdbcTemplate template;

  public List<Apartment> getAll() {
    return List.of();
  }

  public Optional<Apartment> getById(long apartment) {
    return Optional.empty();
  }

  public void add(Apartment apartment) {}

  public void update(Apartment apartment) {}

  public void deleteById(long id) {}
}
