package com.inzynierka2k24.apiserver.web.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.inzynierka2k24.apiserver.exception.reservation.ReservationNotFoundException;
import com.inzynierka2k24.apiserver.exception.reservation.ReservationNotValidException;
import com.inzynierka2k24.apiserver.model.Reservation;
import com.inzynierka2k24.apiserver.service.ReservationService;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ReservationController.class)
@AutoConfigureJsonTesters
public class ReservationControllerTest {

  @Autowired private MockMvc mockMvc;
  @Autowired JacksonTester<Reservation> reservationJacksonTester;
  @Autowired JacksonTester<List<Reservation>> reservationListJacksonTester;
  @MockBean ReservationService reservationService;

  @Test
  @WithMockUser
  public void shouldGetAllReturnsListOfReservations() throws Exception {
    // Given
    long apartmentId = 1;
    List<Reservation> expectedReservations =
        List.of(
            new Reservation(
                Optional.empty(),
                apartmentId,
                Instant.EPOCH,
                Instant.EPOCH.plus(1, ChronoUnit.DAYS)),
            new Reservation(
                Optional.empty(),
                apartmentId,
                Instant.EPOCH.plus(10, ChronoUnit.DAYS),
                Instant.EPOCH.plus(12, ChronoUnit.DAYS)));
    when(reservationService.getAll(apartmentId)).thenReturn(expectedReservations);

    // When/Then
    var response =
        mockMvc
            .perform(
                get(String.format("/1/apartment/%s/reservation", apartmentId))
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse();
    verify(reservationService).getAll(apartmentId);
    reservationListJacksonTester
        .parse(response.getContentAsString())
        .assertThat()
        .asList()
        .hasSize(2)
        .containsAll(expectedReservations);
  }

  @Test
  @WithMockUser
  public void shouldGetReturnsReservation() throws Exception {
    // Given
    long apartmentId = 1;
    long reservationId = 1;
    Reservation expectedReservation =
        new Reservation(
            Optional.of(reservationId),
            apartmentId,
            Instant.EPOCH,
            Instant.EPOCH.plus(1, ChronoUnit.DAYS));
    when(reservationService.getById(apartmentId, reservationId)).thenReturn(expectedReservation);

    // When/Then
    var response =
        mockMvc
            .perform(
                get(String.format("/1/apartment/%s/reservation/%s", apartmentId, reservationId))
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse();
    verify(reservationService).getById(apartmentId, reservationId);
    assertEquals(
        expectedReservation,
        reservationJacksonTester.parse(response.getContentAsString()).getObject());
  }

  @Test
  @WithMockUser
  public void shouldAddReservationWithValidData() throws Exception {
    // Given
    long apartmentId = 1;
    Reservation reservation =
        new Reservation(
            Optional.empty(), apartmentId, Instant.EPOCH, Instant.EPOCH.plus(1, ChronoUnit.DAYS));
    doNothing().when(reservationService).add(apartmentId, reservation);

    // When/Then
    mockMvc
        .perform(
            post(String.format("/1/apartment/%s/reservation", apartmentId))
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(reservationJacksonTester.write(reservation).getJson()))
        .andExpect(status().isCreated())
        .andExpect(content().string("Reservation created successfully"));
    verify(reservationService).add(apartmentId, reservation);
  }

  @Test
  @WithMockUser
  public void shouldUpdateReservationWithValidData() throws Exception {
    // Given
    long apartmentId = 1;
    Reservation reservation =
        new Reservation(
            Optional.of(1L), apartmentId, Instant.EPOCH, Instant.EPOCH.plus(1, ChronoUnit.DAYS));
    doNothing().when(reservationService).update(reservation);

    // When/Then
    mockMvc
        .perform(
            put(String.format("/1/apartment/%s/reservation", apartmentId))
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(reservationJacksonTester.write(reservation).getJson()))
        .andExpect(status().isOk())
        .andExpect(content().string("Reservation updated successfully"));
    verify(reservationService).update(reservation);
  }

  @Test
  @WithMockUser
  public void shouldDeleteReservation() throws Exception {
    // Given
    long apartmentId = 1;
    long reservationId = 1;
    doNothing().when(reservationService).deleteById(apartmentId, reservationId);

    // When/Then
    mockMvc
        .perform(
            delete(String.format("/1/apartment/%s/reservation/%s", apartmentId, reservationId))
                .with(csrf()))
        .andExpect(status().isOk())
        .andExpect(content().string("Reservation deleted successfully"));
    verify(reservationService).deleteById(apartmentId, reservationId);
  }

  @Test
  @WithMockUser
  public void shouldNotGetWithNonExistingReservationIdReturns404() throws Exception {
    // Given
    long apartmentId = 1;
    long reservationId = 1;
    doThrow(ReservationNotFoundException.class)
        .when(reservationService)
        .getById(apartmentId, reservationId);

    // When/Then
    mockMvc
        .perform(
            get(String.format("/1/apartment/%s/reservation/%s", apartmentId, reservationId))
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
    verify(reservationService).getById(apartmentId, reservationId);
  }

  @Test
  @WithMockUser
  public void shouldNotAddReservationWithInvalidData() throws Exception {
    // Given
    long apartmentId = 1;
    Reservation reservation =
        new Reservation(
            Optional.empty(), apartmentId, Instant.now().plus(1, ChronoUnit.DAYS), Instant.now());
    doThrow(ReservationNotValidException.class)
        .when(reservationService)
        .add(apartmentId, reservation);

    // When/Then
    mockMvc
        .perform(
            post(String.format("/1/apartment/%s/reservation", apartmentId))
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(reservationJacksonTester.write(reservation).getJson()))
        .andExpect(status().isBadRequest());
    verify(reservationService, never()).add(any(long.class), any(Reservation.class));
  }

  @Test
  @WithMockUser
  public void shouldNotEditReservationWithInvalidData() throws Exception {
    // Given
    long apartmentId = 1;
    Reservation reservation =
        new Reservation(Optional.of(1L), 1L, Instant.now().plus(1, ChronoUnit.DAYS), Instant.now());
    doThrow(ReservationNotValidException.class).when(reservationService).update(reservation);

    // When/Then
    mockMvc
        .perform(
            put(String.format("/1/apartment/%s/reservation", apartmentId))
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(reservationJacksonTester.write(reservation).getJson()))
        .andExpect(status().isBadRequest());
    verify(reservationService, never()).update(any(Reservation.class));
  }

  @Test
  public void shouldReturnTrueIfStartDateIsAfterEndDate() {
    // Given
    ReservationController controller = new ReservationController(mock(ReservationService.class));
    Reservation reservation =
        new Reservation(
            Optional.empty(), 1L, Instant.now().plus(1, ChronoUnit.DAYS), Instant.now());

    // When/Then
    assertTrue(controller.isNotValid(reservation));
  }

  @Test
  public void shouldReturnFalseIfReservationIsValid() {
    // Given
    ReservationController controller = new ReservationController(mock(ReservationService.class));
    Reservation reservation =
        new Reservation(
            Optional.empty(), 1L, Instant.now(), Instant.now().plus(1, ChronoUnit.DAYS));

    // When/Then
    assertFalse(controller.isNotValid(reservation));
  }
}
