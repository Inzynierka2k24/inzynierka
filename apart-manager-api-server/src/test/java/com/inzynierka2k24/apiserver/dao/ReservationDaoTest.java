package com.inzynierka2k24.apiserver.dao;

import static org.junit.jupiter.api.Assertions.*;

import com.inzynierka2k24.apiserver.model.Reservation;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.annotation.DirtiesContext;

@JdbcTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ReservationDaoTest {

  @Autowired private JdbcTemplate template;

  private ReservationDao reservationDao;

  @BeforeEach
  void setUp(@Autowired DataSource dataSource) throws SQLException {
    try (Connection connection = dataSource.getConnection()) {
      ScriptUtils.executeSqlScript(connection, new ClassPathResource("db-schema.sql"));
      ScriptUtils.executeSqlScript(connection, new ClassPathResource("reservations-test-data.sql"));
    }

    reservationDao = new ReservationDao(template);
  }

  @Test
  void shouldRetrieveAllReservationsForApartmentId() {
    // Given
    long apartmentId = 1L;
    int expectedSize = 1;
    var expectedReservations =
        List.of(
            new Reservation(
                Optional.of(1L),
                apartmentId,
                Instant.parse("2023-01-01T00:00:00Z"),
                Instant.parse("2023-01-04T00:00:00Z")));

    // When
    List<Reservation> reservations = reservationDao.getAll(apartmentId);

    // Then
    assertEquals(expectedSize, reservations.size());
    assertEquals(expectedReservations, reservations);
  }

  // Can retrieve a reservation by its ID and apartment ID
//  @Test
//  public void test_retrieve_reservation_by_id_and_apartment_id() {
//    // Given
//    long apartmentId = 1;
//    long reservationId = 1;
//
//    // When
//    Optional<Reservation> reservation = reservationDao.getById(apartmentId, reservationId);
//
//    // Then
//    assertTrue(reservation.isPresent());
//    assertEquals(apartmentId, reservation.get().apartmentId());
//    assertEquals(reservationId, reservation.get().id().orElseThrow());
//  }
//
//  // Can add a new reservation for a given apartment ID
//  @Test
//  public void test_addNewReservationForApartmentId() {
//    // Given
//    long apartmentId = 1;
//    Reservation reservation =
//        new Reservation(
//            Optional.empty(),
//            apartmentId,
//            Instant.parse("2022-01-01T00:00:00Z"),
//            Instant.parse("2022-01-02T00:00:00Z"));
//
//    // When
//    reservationDao.add(apartmentId, reservation);
//
//    // Then
//  }
//
//  // Can delete a reservation by its ID and apartment ID
//  @Test
//  public void test_deleteReservationByIdAndApartmentId() {
//    // Given
//    long apartmentId = 1;
//    long reservationId = 1;
//
//    // When
//    reservationDao.deleteById(apartmentId, reservationId);
//
//    // Then
//    // Verify the delete operation by checking if the reservation is no longer present in the
//    // database
//    Optional<Reservation> deletedReservation = reservationDao.getById(apartmentId, reservationId);
//    assertFalse(deletedReservation.isPresent());
//  }
//
//  // Can update an existing reservation
//  @Test
//  public void test_updateExistingReservation() {
//    // Given
//    Reservation existingReservation =
//        new Reservation(
//            Optional.of(1L),
//            1L,
//            Instant.parse("2022-01-01T00:00:00Z"),
//            Instant.parse("2022-01-02T00:00:00Z"));
//    Reservation updatedReservation =
//        new Reservation(
//            Optional.of(1L),
//            1L,
//            Instant.parse("2022-01-03T00:00:00Z"),
//            Instant.parse("2022-01-04T00:00:00Z"));
//
//    // When
//    reservationDao.update(updatedReservation);
//
//    // Then
//    Optional<Reservation> retrievedReservation = reservationDao.getById(1L, 1L);
//    assertTrue(retrievedReservation.isPresent());
//    assertEquals(updatedReservation, retrievedReservation.get());
//  }
//
//  // Can check if a time period is free for a given reservation
//  @Test
//  public void test_checkTimePeriodFree() {
//    // Given
//    Reservation reservation =
//        new Reservation(
//            Optional.empty(),
//            1L,
//            Instant.parse("2022-01-01T00:00:00Z"),
//            Instant.parse("2022-01-05T00:00:00Z"));
//
//    // When
//    boolean isFree = reservationDao.isTimePeriodFree(reservation);
//
//    // Then
//    assertTrue(isFree);
//  }
//
//  // Can handle overlapping reservations when checking if a time period is free
//  @Test
//  public void test_handle_overlapping_reservations() {
//    // Given
//    long apartmentId = 1;
//    Instant startDate1 = Instant.parse("2022-01-01T00:00:00Z");
//    Instant endDate1 = Instant.parse("2022-01-05T00:00:00Z");
//    Reservation reservation1 = new Reservation(Optional.empty(), apartmentId, startDate1, endDate1);
//    Instant startDate2 = Instant.parse("2022-01-03T00:00:00Z");
//    Instant endDate2 = Instant.parse("2022-01-07T00:00:00Z");
//    Reservation reservation2 = new Reservation(Optional.empty(), apartmentId, startDate2, endDate2);
//
//    // When
//    boolean isPeriodFree1 = reservationDao.isTimePeriodFree(reservation1);
//    boolean isPeriodFree2 = reservationDao.isTimePeriodFree(reservation2);
//
//    // Then
//    assertFalse(isPeriodFree1);
//    assertFalse(isPeriodFree2);
//  }
//
//  // Can handle empty result set when retrieving reservations
//  @Test
//  public void test_handle_empty_result_set() {
//    // Given
//    long apartmentId = 1;
//
//    // When
//    List<Reservation> reservations = reservationDao.getAll(apartmentId);
//
//    // Then
//    assertTrue(reservations.isEmpty());
//  }
//
//  // Can handle invalid apartment ID when retrieving reservations
//  @Test
//  public void test_invalid_apartment_id_when_retrieving_reservations() {
//    // Given
//    long invalidApartmentId = -1;
//
//    // When
//    List<Reservation> reservations = reservationDao.getAll(invalidApartmentId);
//
//    // Then
//    assertTrue(reservations.isEmpty());
//  }
//
//  // Can handle empty result set when retrieving a reservation by ID
//  @Test
//  public void test_handle_empty_result_set_when_retrieving_reservation_by_id() {
//    // Given
//    long apartmentId = 1;
//    long reservationId = 1;
//
//    // When
//    Optional<Reservation> reservation = reservationDao.getById(apartmentId, reservationId);
//
//    // Then
//    assertTrue(reservation.isEmpty());
//  }
//
//  // Can handle invalid apartment ID when retrieving a reservation by ID
//  @Test
//  public void test_handle_invalid_apartment_id_when_retrieving_reservation_by_id() {
//    // Given
//    long invalidApartmentId = -1;
//    long reservationId = 1;
//
//    // When
//    Optional<Reservation> reservation = reservationDao.getById(invalidApartmentId, reservationId);
//
//    // Then
//    assertFalse(reservation.isPresent());
//  }
//
//  // Can handle invalid reservation ID when retrieving a reservation by ID
//  @Test
//  public void test_handle_invalid_reservation_id() {
//    // Given
//    long apartmentId = 1;
//    long reservationId = 100;
//
//    // When
//    Optional<Reservation> reservation = reservationDao.getById(apartmentId, reservationId);
//
//    // Then
//    assertTrue(reservation.isEmpty());
//  }
//
//  // Can handle invalid reservation ID when updating a reservation
//  @Test
//  public void test_handle_invalid_reservation_id_when_updating_reservation() {
//    // Given
//    long apartmentId = 1;
//    long reservationId = 100;
//
//    // When
//    Optional<Reservation> reservation = reservationDao.getById(apartmentId, reservationId);
//
//    // Then
//    assertFalse(reservation.isPresent());
//  }
//
//  // Can handle invalid apartment ID when deleting a reservation
//  @Test
//  public void test_handle_invalid_apartment_id_when_deleting_reservation() {
//    // Given
//    long apartmentId = 1;
//    long reservationId = 1;
//
//    // When
//    reservationDao.deleteById(apartmentId, reservationId);
//
//    // Then
//    // Verify the delete operation does not throw any exception
//  }
//
//  // Can handle invalid reservation ID when deleting a reservation
//  @Test
//  public void test_handle_invalid_reservation_id_when_deleting_reservation() {
//    // Given
//    long apartmentId = 1;
//    long reservationId = 100;
//
//    // When
//    reservationDao.deleteById(apartmentId, reservationId);
//
//    // Then
//    // Verify that no exception is thrown and the delete operation is successful
//  }
}
