package com.inzynierka2k24.apiserver.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.inzynierka2k24.apiserver.dao.ApartmentDao;
import com.inzynierka2k24.apiserver.exception.apartment.ApartmentNotFoundException;
import com.inzynierka2k24.apiserver.model.Apartment;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

public class ApartmentServiceTest {

  private final ApartmentDao apartmentDao = mock(ApartmentDao.class);
  private final ApartmentService apartmentService = new ApartmentService(apartmentDao);

  @Test
  public void shouldGetAllApartmentsForUserId() {
    // Given
    long userId = 1;
    List<Apartment> expectedApartments = new ArrayList<>();
    expectedApartments.add(
        new Apartment(
            Optional.of(1L),
            100.0f,
            "Apartment 1",
            "Country 1",
            "City 1",
            "Street 1",
            "Building 1",
            "Apartment 1",
            4));
    expectedApartments.add(
        new Apartment(
            Optional.of(2L),
            200.0f,
            "Apartment 2",
            "Country 2",
            "City 2",
            "Street 2",
            "Building 2",
            "Apartment 2",
            4));
    when(apartmentDao.getAll(userId)).thenReturn(expectedApartments);

    // When
    List<Apartment> actualApartments = apartmentService.getAll(userId);

    // Then
    assertEquals(expectedApartments, actualApartments);
  }

  @Test
  public void shouldGetApartmentByIdForUserId() throws ApartmentNotFoundException {
    // Given
    long userId = 1;
    long apartmentId = 1;
    Apartment expectedApartment =
        new Apartment(
            Optional.of(1L),
            100.0f,
            "Apartment 1",
            "Country 1",
            "City 1",
            "Street 1",
            "Building 1",
            "Apartment 1",
            4);
    when(apartmentDao.getById(userId, apartmentId)).thenReturn(Optional.of(expectedApartment));

    // When
    Apartment actualApartment = apartmentService.getById(userId, apartmentId);

    // Then
    assertEquals(expectedApartment, actualApartment);
  }

  @Test
  public void shouldAddApartmentForUserId() {
    // Given
    long userId = 1;
    Apartment apartment =
        new Apartment(
            Optional.empty(),
            100.0f,
            "Apartment 1",
            "Country 1",
            "City 1",
            "Street 1",
            "Building 1",
            "Apartment 1",
            4);

    // When
    apartmentService.add(userId, apartment);

    // Then
    verify(apartmentDao).add(userId, apartment);
  }

  @Test
  public void shouldUpdateExistingApartmentForUserIdAndApartmentId()
      throws ApartmentNotFoundException {
    // Given
    long userId = 1;
    long apartmentId = 1;
    Apartment apartment =
        new Apartment(
            Optional.of(1L),
            100.0f,
            "Updated Apartment",
            "Updated Country",
            "Updated City",
            "Updated Street",
            "Updated Building",
            "Updated Apartment",
            4);
    when(apartmentDao.getById(userId, apartmentId)).thenReturn(Optional.of(apartment));

    // When
    apartmentService.update(userId, apartment);

    // Then
    verify(apartmentDao).update(userId, apartment);
  }

  @Test
  public void shouldDeleteExistingApartmentForUserIdAndApartmentId()
      throws ApartmentNotFoundException {
    // Given
    long userId = 1;
    long apartmentId = 1;
    when(apartmentDao.getById(userId, apartmentId))
        .thenReturn(
            Optional.of(
                new Apartment(
                    Optional.of(1L),
                    100.0f,
                    "Apartment 1",
                    "Country 1",
                    "City 1",
                    "Street 1",
                    "Building 1",
                    "Apartment 1",
                    4)));

    // When
    apartmentService.deleteById(userId, apartmentId);

    // Then
    verify(apartmentDao).deleteById(userId, apartmentId);
  }

  @Test
  public void
      shouldThrowApartmentNotFoundExceptionWhenGettingNonExistingApartmentForUserIdAndApartmentId() {
    // Given
    long userId = 1;
    long apartmentId = 1;
    when(apartmentDao.getById(userId, apartmentId)).thenReturn(Optional.empty());

    // When/Then
    assertThrows(
        ApartmentNotFoundException.class, () -> apartmentService.getById(userId, apartmentId));
  }

  @Test
  public void shouldThrowIllegalArgumentExceptionWhenAddingApartmentNotBelongingToUserId() {
    // Given
    long userId = 1;
    Apartment apartment =
        new Apartment(
            Optional.empty(),
            100.0f,
            "Apartment 1",
            "Country 1",
            "City 1",
            "Street 1",
            "Building 1",
            "Apartment 1",
            4);
    doThrow(new IllegalArgumentException()).when(apartmentDao).add(userId, apartment);

    // When/Then
    assertThrows(IllegalArgumentException.class, () -> apartmentService.add(userId, apartment));
  }

  @Test
  public void
      shouldThrowApartmentNotFoundExceptionWhenUpdatingNonExistingApartmentForUserIdAndApartmentId() {
    // Given
    long userId = 1;
    long apartmentId = 1;
    Apartment apartment =
        new Apartment(
            Optional.of(1L),
            100.0f,
            "Updated Apartment",
            "Updated Country",
            "Updated City",
            "Updated Street",
            "Updated Building",
            "Updated Apartment",
            4);
    when(apartmentDao.getById(userId, apartmentId)).thenReturn(Optional.empty());

    // When/Then
    assertThrows(
        ApartmentNotFoundException.class, () -> apartmentService.update(userId, apartment));
  }

  @Test
  public void
      shouldThrowApartmentNotFoundExceptionWhenDeletingNonExistingApartmentForUserIdAndApartmentId() {
    // Given
    long userId = 1;
    long apartmentId = 1;
    when(apartmentDao.getById(userId, apartmentId)).thenReturn(Optional.empty());

    // When/Then
    assertThrows(
        ApartmentNotFoundException.class, () -> apartmentService.deleteById(userId, apartmentId));
  }

  @Test
  public void testApartmentExistsForUserIdAndApartmentId() {
    // Given
    long userId = 1;
    long apartmentId = 1;
    when(apartmentDao.getById(userId, apartmentId))
        .thenReturn(
            Optional.of(
                new Apartment(
                    Optional.of(1L),
                    100.0f,
                    "Apartment 1",
                    "Country 1",
                    "City 1",
                    "Street 1",
                    "Building 1",
                    "Apartment 1",
                    4)));

    // When
    boolean exists = apartmentService.existsById(userId, apartmentId);

    // Then
    assertTrue(exists);
  }

  @Test
  public void testApartmentDoesNotExistForUserIdAndApartmentId() {
    // Given
    long userId = 1;
    long apartmentId = 1;
    when(apartmentDao.getById(userId, apartmentId)).thenReturn(Optional.empty());

    // When
    boolean exists = apartmentService.existsById(userId, apartmentId);

    // Then
    assertFalse(exists);
  }

  @Test
  public void shouldNotUpdateApartmentIfIdNotPresent() {
    // Given
    long userId = 1;
    Apartment apartment =
        new Apartment(
            Optional.empty(),
            100.0f,
            "Apartment 1",
            "Country 1",
            "City 1",
            "Street 1",
            "Building 1",
            "Apartment 1",
            4);

    // When
    assertThrows(
        ApartmentNotFoundException.class, () -> apartmentService.update(userId, apartment));

    // Then
    verify(apartmentDao, never()).update(anyLong(), any(Apartment.class));
  }
}
