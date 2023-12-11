package com.inzynierka2k24.apiserver.service;

import com.inzynierka2k24.apiserver.dao.FinanceSourceDao;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Map;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class FinanceSourceServiceTest {

  @Mock
  private FinanceSourceDao financeSourceDao;

  @InjectMocks
  private FinanceSourceService financeSourceService;

  @Test
  void getEventsWithSourcesShouldReturnCorrectData() {
    // Given
    var mockResponse = Map.of(
        "RESERVATION", List.of("BOOKING", "AIRBNB"),
        "RENOVATION", List.of("CLEANING", "REPAIR")
    );
    when(financeSourceDao.getAll()).thenReturn(mockResponse);

    // When
    final var result = financeSourceService.getEventsWithSources();

    // Then
    assertEquals(mockResponse, result);
  }
}
