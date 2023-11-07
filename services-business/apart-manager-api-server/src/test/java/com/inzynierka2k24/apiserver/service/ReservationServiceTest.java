package com.inzynierka2k24.apiserver.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.inzynierka2k24.apiserver.dao.ReservationDao;
import com.inzynierka2k24.apiserver.exception.reservation.ReservationNotFoundException;
import com.inzynierka2k24.apiserver.exception.reservation.ReservationNotValidException;
import com.inzynierka2k24.apiserver.model.Reservation;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class ReservationServiceTest {

  private final ReservationDao reservationDao = mock(ReservationDao.class);
  private final ReservationService reservationService = new ReservationService(reservationDao);

  @Test
  public void shouldGetAllReservationsForApartmentId() {
    // Given
    long apartmentId = 1;
    List<Reservation> expectedReservations = new ArrayList<>();
    expectedReservations.add(
        new Reservation(
            Optional.of(1L),
            1L,
            Instant.parse("2022-01-01T00:00:00Z"),
            Instant.parse("2022-01-02T00:00:00Z")));
    expectedReservations.add(
        new Reservation(
            Optional.of(2L),
            1L,
            Instant.parse("2022-01-03T00:00:00Z"),
            Instant.parse("2022-01-04T00:00:00Z")));
    when(reservationDao.getAll(apartmentId)).thenReturn(expectedReservations);

    // When
    List<Reservation> actualReservations = reservationService.getAll(apartmentId);

    // Then
    assertEquals(expectedReservations, actualReservations);
    verify(reservationDao).getAll(apartmentId);
  }

  @Test
  public void shouldThrowWhenReservationNotFound() {
    // Given
    long apartmentId = 1;
    long reservationId = 1;
    when(reservationDao.getById(apartmentId, reservationId)).thenReturn(Optional.empty());

    // When/Then
    assertThrows(
        ReservationNotFoundException.class,
        () -> reservationService.getById(apartmentId, reservationId));
    verify(reservationDao).getById(apartmentId, reservationId);
  }

  @Test
  public void shouldAddReservationWhenTimePeriodIsFree() throws ReservationNotValidException {
    // Given
    long apartmentId = 1;
    Reservation reservation =
        new Reservation(
            Optional.empty(),
            1L,
            Instant.parse("2022-01-01T00:00:00Z"),
            Instant.parse("2022-01-02T00:00:00Z"));
    when(reservationDao.isTimePeriodFree(reservation)).thenReturn(true);

    // When
    reservationService.add(apartmentId, reservation);

    // Then
    verify(reservationDao).add(apartmentId, reservation);
  }

  @Test
  public void shouldNotAddReservationWhenTimePeriodIsNotFree() {
    // Given
    long apartmentId = 1;
    Reservation reservation =
        new Reservation(
            Optional.empty(),
            1L,
            Instant.parse("2022-01-01T00:00:00Z"),
            Instant.parse("2022-01-02T00:00:00Z"));
    when(reservationDao.isTimePeriodFree(reservation)).thenReturn(false);

    // When/Then
    assertThrows(
        ReservationNotValidException.class, () -> reservationService.add(apartmentId, reservation));
    verify(reservationDao, Mockito.never()).add(apartmentId, reservation);
  }

  @Test
  public void shouldUpdateReservationWhenTimePeriodIsFreeAndIdPresent()
      throws ReservationNotValidException {
    // Given
    Reservation reservation =
        new Reservation(
            Optional.of(1L),
            1L,
            Instant.parse("2022-01-01T00:00:00Z"),
            Instant.parse("2022-01-02T00:00:00Z"));
    when(reservationDao.isTimePeriodFree(reservation)).thenReturn(true);

    // When
    reservationService.update(reservation);

    // Then
    verify(reservationDao).update(reservation);
  }

  @Test
  public void shouldNotUpdateReservationWhenTimePeriodIsNotFreeOrIdNotPresent() {
    // Given
    Reservation reservation =
        new Reservation(
            Optional.empty(),
            1L,
            Instant.parse("2022-01-01T00:00:00Z"),
            Instant.parse("2022-01-02T00:00:00Z"));
    when(reservationDao.isTimePeriodFree(reservation)).thenReturn(false);

    // When/Then
    assertThrows(ReservationNotValidException.class, () -> reservationService.update(reservation));
    verify(reservationDao, Mockito.never()).update(reservation);
  }

  @Test
  public void shouldDeleteReservation() {
    // Given
    long apartmentId = 1;
    long reservationId = 1;

    // When
    reservationService.deleteById(apartmentId, reservationId);

    // Then
    verify(reservationDao).deleteById(apartmentId, reservationId);
  }

  @Test
  public void shouldReceiveReservationById() throws ReservationNotFoundException {
    // Given
    long apartmentId = 1;
    long reservationId = 1;
    Reservation expectedReservation =
        new Reservation(
            Optional.of(1L),
            apartmentId,
            Instant.parse("2022-01-01T00:00:00Z"),
            Instant.parse("2022-01-02T00:00:00Z"));
    when(reservationDao.getById(apartmentId, reservationId))
        .thenReturn(Optional.of(expectedReservation));

    // When
    Reservation actualReservation = reservationService.getById(apartmentId, reservationId);

    // Then
    assertEquals(expectedReservation, actualReservation);
  }

  @Test
  public void shouldThrowReservationNotFoundException() {
    // Given
    long apartmentId = 1;
    long reservationId = 100;
    when(reservationDao.getById(apartmentId, reservationId)).thenReturn(Optional.empty());

    // When
    assertThrows(
        ReservationNotFoundException.class,
        () -> {
          reservationService.getById(apartmentId, reservationId);
        });

    // Then
    verify(reservationDao).getById(apartmentId, reservationId);
  }
}
