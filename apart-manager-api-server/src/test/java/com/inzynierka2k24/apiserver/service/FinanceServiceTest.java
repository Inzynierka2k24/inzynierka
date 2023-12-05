package com.inzynierka2k24.apiserver.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.inzynierka2k24.apiserver.dao.FinanceDao;
import com.inzynierka2k24.apiserver.exception.finance.FinanceNotFoundException;
import com.inzynierka2k24.apiserver.model.Finance;
import com.inzynierka2k24.apiserver.web.dto.FinanceDTO;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

public class FinanceServiceTest {

  private final FinanceDao financeDao = mock(FinanceDao.class);
  private final FinanceService financeService = new FinanceService(financeDao);

  @Test
  public void shouldGetAllFinancesForUserId() {
    // Given
    long userId = 1;
    List<Finance> expectedFinances = new ArrayList<>();
    expectedFinances.add(
        new Finance(
            Optional.of(1L),
            1L,
            1L,
            1,
            1,
            200.5f,
            Instant.parse("2023-01-01T00:00:00Z"),
            "Washing machine repair"));
    expectedFinances.add(
        new Finance(
            Optional.of(2L), 1L, 1L, 1, 1, 200.5f, Instant.parse("2023-01-01T00:00:00Z"), "none"));

    when(financeDao.getAll(userId)).thenReturn(expectedFinances);

    // When
    List<Finance> actualFinances = financeService.getAll(userId);

    // Then
    assertEquals(expectedFinances, actualFinances);
  }

  @Test
  public void shouldGetFinanceById() throws FinanceNotFoundException {
    // Given
    long financeId = 1;
    Finance expectedFinance =
        new Finance(
            Optional.of(1L),
            1L,
            1L,
            1,
            1,
            200.5f,
            Instant.parse("2023-01-01T00:00:00Z"),
            "Washing machine repair");

    when(financeDao.getById(financeId)).thenReturn(Optional.of(expectedFinance));

    // When
    Finance actualFinance = financeService.getById(financeId);

    // Then
    assertEquals(expectedFinance, actualFinance);
  }

  @Test
  public void shouldGetFinanceByApartmentId() {
    // Given
    long apartmentId = 1;
    List<Finance> expectedFinances = new ArrayList<>();
    expectedFinances.add(
        new Finance(
            Optional.of(1L),
            1L,
            1L,
            1,
            1,
            200.5f,
            Instant.parse("2023-01-01T00:00:00Z"),
            "Washing machine repair"));
    expectedFinances.add(
        new Finance(
            Optional.of(2L), 1L, 1L, 1, 1, 200.5f, Instant.parse("2023-01-01T00:00:00Z"), "none"));

    when(financeDao.getByApartmentId(apartmentId)).thenReturn(expectedFinances);

    // When
    List<Finance> actualFinances = financeService.getByApartmentId(apartmentId);

    // Then
    assertEquals(expectedFinances, actualFinances);
  }

  @Test
  public void shouldAddFinance() {
    // Given
    Finance finance =
        new Finance(
            Optional.empty(),
            1L,
            33L,
            1,
            103,
            200.5f,
            Instant.parse("2023-01-01T00:00:00Z"),
            "none");
    FinanceDTO financeDto =
        new FinanceDTO(
            1L,
            33L,
            "RESERVATION",
            "NOCOWANIEPL",
            200.5f,
            Instant.parse("2023-01-01T00:00:00Z"),
            "none");
    // When
    financeService.add(financeDto);

    // Then
    verify(financeDao).add(finance);
  }

  @Test
  public void shouldUpdateExistingFinanceForFinanceId() throws FinanceNotFoundException {
    // Given
    long financeId = 1;
    Finance finance =
        new Finance(
            Optional.of(1L), 1L, 1L, 1, 1, 3333f, Instant.parse("2023-01-01T00:00:00Z"), "none");

    when(financeDao.getById(financeId)).thenReturn(Optional.of(finance));

    // When
    financeService.update(finance);

    // Then
    verify(financeDao).update(finance);
  }

  @Test
  public void shouldDeleteExistingFinanceForFinanceId() throws FinanceNotFoundException {
    // Given
    long financeId = 1;
    when(financeDao.getById(financeId))
        .thenReturn(
            Optional.of(
                new Finance(
                    Optional.of(1L),
                    1L,
                    1L,
                    1,
                    1,
                    3333f,
                    Instant.parse("2023-01-01T00:00:00Z"),
                    "none")));

    // When
    financeService.deleteById(financeId);

    // Then
    verify(financeDao).deleteById(financeId);
  }

  @Test
  public void shouldThrowFinanceNotFoundExceptionWhenGettingNonExistingFinanceForFinanceId() {
    // Given
    long financeId = 33;
    when(financeDao.getById(financeId)).thenReturn(Optional.empty());

    // When/Then
    assertThrows(FinanceNotFoundException.class, () -> financeService.getById(financeId));
  }

  @Test
  public void shouldThrowIllegalArgumentExceptionWhenAddingFinanceToNonExistingUserId() {
    // Given
    Finance finance =
        new Finance(
            Optional.empty(), 1L, 33L, 1, 8, 200.5f, Instant.parse("2023-01-01T00:00:00Z"), "none");
    FinanceDTO financeDto =
        new FinanceDTO(
            1L,
            33L,
            "RESERVATION",
            "NOCOWANIE",
            200.5f,
            Instant.parse("2023-01-01T00:00:00Z"),
            "none");
    doThrow(new IllegalArgumentException()).when(financeDao).add(finance);

    // When/Then
    assertThrows(IllegalArgumentException.class, () -> financeService.add(financeDto));
  }

  @Test
  public void shouldThrowIllegalArgumentExceptionWhenAddingFinanceToNonExistingApartmentId() {
    // Given
    Finance finance =
        new Finance(
            Optional.empty(), 1L, 33L, 1, 8, 200.5f, Instant.parse("2023-01-01T00:00:00Z"), "none");
    FinanceDTO financeDto =
        new FinanceDTO(
            1L,
            33L,
            "RESERVATION",
            "NOCOWANIE",
            200.5f,
            Instant.parse("2023-01-01T00:00:00Z"),
            "none");
    doThrow(new IllegalArgumentException()).when(financeDao).add(finance);

    // When/Then
    assertThrows(IllegalArgumentException.class, () -> financeService.add(financeDto));
  }

  @Test
  public void shouldThrowFinanceNotFoundExceptionWhenUpdatingNonExistingFinanceForFinanceId() {
    // Given
    long financeId = 33;
    Finance finance =
        new Finance(
            Optional.of(33L), 1L, 33L, 1, 1, 200.5f, Instant.parse("2023-01-01T00:00:00Z"), "none");
    when(financeDao.getById(financeId)).thenReturn(Optional.empty());

    // When/Then
    assertThrows(FinanceNotFoundException.class, () -> financeService.update(finance));
  }

  @Test
  public void shouldThrowFinanceNotFoundExceptionWhenUpdatingFinanceForNonExistingUserId() {
    // Given
    Finance finance =
        new Finance(
            Optional.of(1L), 33L, 1L, 1, 1, 200.5f, Instant.parse("2023-01-01T00:00:00Z"), "none");
    doThrow(new IllegalArgumentException()).when(financeDao).update(finance);

    // When/Then
    assertThrows(FinanceNotFoundException.class, () -> financeService.update(finance));
  }

  @Test
  public void shouldThrowIllegalArgumentExceptionWhenUpdatingFinanceForNonExistingApartmentId() {
    // Given
    Finance finance =
        new Finance(
            Optional.of(1L), 1L, 33L, 1, 1, 200.5f, Instant.parse("2023-01-01T00:00:00Z"), "none");
    doThrow(new IllegalArgumentException()).when(financeDao).update(finance);

    // When/Then
    assertThrows(FinanceNotFoundException.class, () -> financeService.update(finance));
  }

  @Test
  public void shouldThrowFinanceNotFoundExceptionWhenDeletingNonExistingFinanceForFinanceId() {
    // Given
    long financeId = 33;
    when(financeDao.getById(financeId)).thenReturn(Optional.empty());

    // When/Then
    assertThrows(FinanceNotFoundException.class, () -> financeService.deleteById(financeId));
  }

  @Test
  public void testFinanceExistsForFinanceId() {
    // Given
    long financeId = 1;
    when(financeDao.getById(financeId))
        .thenReturn(
            Optional.of(
                new Finance(
                    Optional.of(1L),
                    1L,
                    1L,
                    1,
                    1,
                    200.5f,
                    Instant.parse("2023-01-01T00:00:00Z"),
                    "Washing machine repair")));

    // When
    boolean exists = financeService.existsById(financeId);

    // Then
    assertTrue(exists);
  }

  @Test
  public void testFinanceDoesNotExistForFinanceId() {
    // Given
    long financeId = 33;
    when(financeDao.getById(financeId)).thenReturn(Optional.empty());

    // When
    boolean exists = financeService.existsById(financeId);

    // Then
    assertFalse(exists);
  }

  @Test
  public void shouldNotUpdateFinanceIfIdNotPresent() {
    // Given
    Finance finance =
        new Finance(
            Optional.empty(), 1L, 1L, 1, 1, 200.5f, Instant.parse("2023-01-01T00:00:00Z"), "none");

    // When
    assertThrows(FinanceNotFoundException.class, () -> financeService.update(finance));

    // Then
    verify(financeDao, never()).update(any(Finance.class));
  }
}
