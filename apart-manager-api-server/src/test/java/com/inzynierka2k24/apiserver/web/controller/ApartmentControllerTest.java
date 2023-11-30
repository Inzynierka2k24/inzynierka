package com.inzynierka2k24.apiserver.web.controller;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.inzynierka2k24.apiserver.exception.apartment.ApartmentNotFoundException;
import com.inzynierka2k24.apiserver.model.Apartment;
import com.inzynierka2k24.apiserver.service.ApartmentService;
import java.util.ArrayList;
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

@WebMvcTest(ApartmentController.class)
@AutoConfigureJsonTesters
public class ApartmentControllerTest {

  @Autowired JacksonTester<Apartment> apartmentJacksonTester;
  @Autowired JacksonTester<List<Apartment>> apartmentListJacksonTester;
  @MockBean ApartmentService apartmentService;
  @Autowired private MockMvc mockMvc;

  @Test
  @WithMockUser
  public void shouldGetAllApartmentsForGivenUserId() throws Exception {
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
    when(apartmentService.getAll(userId)).thenReturn(expectedApartments);

    // When/Then
    var response =
        mockMvc
            .perform(
                get(String.format("/%s/apartments", userId)).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse();
    verify(apartmentService).getAll(userId);
    apartmentListJacksonTester
        .parse(response.getContentAsString())
        .assertThat()
        .asList()
        .hasSize(2)
        .containsAll(expectedApartments);
  }

  @Test
  @WithMockUser
  public void shouldGetApartmentForGivenUserIdAndApartmentId() throws Exception {
    // Given
    long userId = 1;
    long apartmentId = 1;
    Apartment expectedApartment =
        new Apartment(
            Optional.of(apartmentId),
            100.0f,
            "Apartment 1",
            "Country 1",
            "City 1",
            "Street 1",
            "Building 1",
            "Apartment 1",
            4);
    when(apartmentService.getById(userId, apartmentId)).thenReturn(expectedApartment);

    // When/Then
    var response =
        mockMvc
            .perform(
                get(String.format("/%s/apartments/%s", userId, apartmentId))
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse();
    verify(apartmentService).getById(userId, apartmentId);
    assertEquals(
        expectedApartment, apartmentJacksonTester.parse(response.getContentAsString()).getObject());
  }

  @Test
  @WithMockUser
  public void shouldAddApartmentForGivenUserId() throws Exception {
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
    doNothing().when(apartmentService).add(userId, apartment);

    // When/Then
    mockMvc
        .perform(
            post(String.format("/%s/apartments", userId))
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(apartmentJacksonTester.write(apartment).getJson()))
        .andExpect(status().isCreated())
        .andExpect(content().string("Apartment created successfully"));
    verify(apartmentService).add(userId, apartment);
  }

  @Test
  @WithMockUser
  public void shouldEditExistingApartmentForGivenUserId() throws Exception {
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
    doNothing().when(apartmentService).update(userId, apartment);

    // When/Then
    mockMvc
        .perform(
            put(String.format("/%s/apartments", userId))
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(apartmentJacksonTester.write(apartment).getJson()))
        .andExpect(status().isOk())
        .andExpect(content().string("Apartment edited successfully"));
    verify(apartmentService).update(userId, apartment);
  }

  @Test
  @WithMockUser
  public void shouldDeleteExistingApartmentForGivenUserIdAndApartmentId() throws Exception {
    // Given
    long userId = 1;
    long apartmentId = 1;
    doNothing().when(apartmentService).deleteById(userId, apartmentId);

    // When/Then
    mockMvc
        .perform(delete(String.format("/%s/apartments/%s", userId, apartmentId)).with(csrf()))
        .andExpect(status().isOk())
        .andExpect(content().string("Apartment deleted successfully"));
    verify(apartmentService).deleteById(userId, apartmentId);
  }

  @Test
  @WithMockUser
  public void shouldGetNoApartmentsForUserWithNoApartments() throws Exception {
    // Given
    long userId = 1;
    List<Apartment> expectedApartments = emptyList();
    when(apartmentService.getAll(userId)).thenReturn(expectedApartments);

    // When/Then
    var response =
        mockMvc
            .perform(get(String.format("/%s/apartments", userId)))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse();
    verify(apartmentService).getAll(userId);
    apartmentListJacksonTester.parse(response.getContentAsString()).assertThat().asList().isEmpty();
  }

  @Test
  @WithMockUser
  public void shouldThrowWhenGetNonExistentApartment() throws Exception {
    // Given
    long userId = 1;
    long apartmentId = 2;
    doThrow(ApartmentNotFoundException.class).when(apartmentService).getById(userId, apartmentId);

    // When/Then
    mockMvc
        .perform(get(String.format("/%s/apartments/%s", userId, apartmentId)))
        .andExpect(status().isNotFound());
    verify(apartmentService).getById(userId, apartmentId);
  }

  @Test
  @WithMockUser
  public void shouldNotUpdateNonExistentApartment() throws Exception {
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
    doThrow(ApartmentNotFoundException.class).when(apartmentService).update(userId, apartment);

    // When/Then
    mockMvc
        .perform(
            put(String.format("/%s/apartments", userId))
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(apartmentJacksonTester.write(apartment).getJson()))
        .andExpect(status().isNotFound());
    verify(apartmentService).update(userId, apartment);
  }

  @Test
  @WithMockUser
  public void shouldNotDeleteNonExistentApartment() throws Exception {
    // Given
    long userId = 1;
    long apartmentId = 1;
    doThrow(ApartmentNotFoundException.class)
        .when(apartmentService)
        .deleteById(userId, apartmentId);

    // When/Then
    mockMvc
        .perform(delete(String.format("/%s/apartments/%s", userId, apartmentId)).with(csrf()))
        .andExpect(status().isNotFound());
    verify(apartmentService).deleteById(userId, apartmentId);
  }
}
