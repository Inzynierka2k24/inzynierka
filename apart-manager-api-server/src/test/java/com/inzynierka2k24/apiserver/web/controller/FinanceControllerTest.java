package com.inzynierka2k24.apiserver.web.controller;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.inzynierka2k24.apiserver.exception.finance.FinanceNotFoundException;
import com.inzynierka2k24.apiserver.model.Finance;
import com.inzynierka2k24.apiserver.service.FinanceService;
import java.time.Instant;
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

@WebMvcTest(FinanceController.class)
@AutoConfigureJsonTesters
public class FinanceControllerTest {

  @Autowired JacksonTester<Finance> financeJacksonTester;
  @Autowired JacksonTester<List<Finance>> financeListJacksonTester;
  @MockBean FinanceService financeService;
  @Autowired private MockMvc mockMvc;

  @Test
  @WithMockUser
  public void shouldGetAllFinancesForGivenUserId() throws Exception {
    // Given
    long userId = 1;
    List<Finance> expectedFinances = new ArrayList<>();
    expectedFinances.add(
        new Finance(
            Optional.of(1L),
            1L,
            1L,
            1L,
            1,
            1,
            200.5f,
            Instant.parse("2023-01-01T00:00:00Z"),
            "Washing machine repair"));
    expectedFinances.add(
        new Finance(
            Optional.of(2L),
            1L,
            1L,
            1L,
            1,
            1,
            200.5f,
            Instant.parse("2023-01-01T00:00:00Z"),
            "none"));
    when(financeService.getAll(userId)).thenReturn(expectedFinances);

    // When/Then
    var response =
        mockMvc
            .perform(get(String.format("/%s/finance", userId)).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse();
    verify(financeService).getAll(userId);
    financeListJacksonTester
        .parse(response.getContentAsString())
        .assertThat()
        .asList()
        .hasSize(2)
        .containsAll(expectedFinances);
  }

  @Test
  @WithMockUser
  public void shouldGetFinanceForGivenFinanceId() throws Exception {
    // Given
    long userId = 1;
    long financeId = 1;
    Finance expectedFinance =
        new Finance(
            Optional.of(financeId),
            1L,
            1L,
            1L,
            1,
            1,
            200.5f,
            Instant.parse("2023-01-01T00:00:00Z"),
            "Washing machine repair");

    when(financeService.getById(financeId)).thenReturn(expectedFinance);

    // When/Then
    var response =
        mockMvc
            .perform(
                get(String.format("/%s/finance/%s", userId, financeId))
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse();
    verify(financeService).getById(financeId);
    assertEquals(
        expectedFinance, financeJacksonTester.parse(response.getContentAsString()).getObject());
  }

  @Test
  @WithMockUser
  public void shouldGetAllFinancesForGivenApartmentId() throws Exception {
    // Given
    long userId = 1;
    long apartmentId = 1;
    List<Finance> expectedFinances = new ArrayList<>();
    expectedFinances.add(
        new Finance(
            Optional.of(1L),
            1L,
            1L,
            1L,
            1,
            1,
            200.5f,
            Instant.parse("2023-01-01T00:00:00Z"),
            "Washing machine repair"));
    expectedFinances.add(
        new Finance(
            Optional.of(2L),
            1L,
            1L,
            1L,
            1,
            1,
            200.5f,
            Instant.parse("2023-01-01T00:00:00Z"),
            "none"));

    when(financeService.getByApartmentId(apartmentId)).thenReturn(expectedFinances);

    // When/Then
    var response =
        mockMvc
            .perform(
                get(String.format("/%s/finance/apartment/%s", userId, apartmentId))
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse();
    verify(financeService).getByApartmentId(apartmentId);

    financeListJacksonTester
        .parse(response.getContentAsString())
        .assertThat()
        .asList()
        .hasSize(2)
        .containsAll(expectedFinances);
  }

  @Test
  @WithMockUser
  public void shouldAddFinanceForGivenUserId() throws Exception {
    // Given
    long userId = 1;
    Finance finance =
        new Finance(
            Optional.empty(),
            1L,
            1L,
            1L,
            1,
            1,
            200.5f,
            Instant.parse("2023-01-01T00:00:00Z"),
            "Washing machine repair");
    doNothing().when(financeService).add(finance);

    // When/Then
    mockMvc
        .perform(
            post(String.format("/%s/finance", userId))
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(financeJacksonTester.write(finance).getJson()))
        .andExpect(status().isCreated())
        .andExpect(content().string("Finance created successfully"));
    verify(financeService).add(finance);
  }

  @Test
  @WithMockUser
  public void shouldEditExistingFinanceForGivenFinanceId() throws Exception {
    // Given
    long userId = 1;
    long financeId = 1;
    Finance finance =
        new Finance(
            Optional.of(1L),
            1L,
            1L,
            1L,
            1,
            1,
            200.5f,
            Instant.parse("2023-01-01T00:00:00Z"),
            "Washing machine repair");
    doNothing().when(financeService).update(finance);

    // When/Then
    mockMvc
        .perform(
            put(String.format("/%s/finance/%s", userId, financeId))
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(financeJacksonTester.write(finance).getJson()))
        .andExpect(status().isOk())
        .andExpect(content().string("Finance edited successfully"));
    verify(financeService).update(finance);
  }

  @Test
  @WithMockUser
  public void shouldDeleteExistingFinanceForGivenFinanceId() throws Exception {
    // Given
    long userId = 1;
    long financeId = 1;
    doNothing().when(financeService).deleteById(financeId);

    // When/Then
    mockMvc
        .perform(delete(String.format("/%s/finance/%s", userId, financeId)).with(csrf()))
        .andExpect(status().isOk())
        .andExpect(content().string("Finance deleted successfully"));
    verify(financeService).deleteById(financeId);
  }

  @Test
  @WithMockUser
  public void shouldGetNoFinancesForUserWithNoFinances() throws Exception {
    // Given
    long userId = 2;
    List<Finance> expectedFinances = emptyList();
    when(financeService.getAll(userId)).thenReturn(expectedFinances);

    // When/Then
    var response =
        mockMvc
            .perform(get(String.format("/%s/finance", userId)))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse();
    verify(financeService).getAll(userId);
    financeListJacksonTester.parse(response.getContentAsString()).assertThat().asList().isEmpty();
  }

  @Test
  @WithMockUser
  public void shouldGetNoFinancesForApartmentWithNoFinances() throws Exception {
    // Given
    long userId = 2;
    long apartmentId = 2;
    List<Finance> expectedFinances = emptyList();
    when(financeService.getByApartmentId(apartmentId)).thenReturn(expectedFinances);

    // When/Then
    var response =
        mockMvc
            .perform(get(String.format("/%s/finance/apartment/%s", userId, apartmentId)))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse();
    verify(financeService).getByApartmentId(userId);
    financeListJacksonTester.parse(response.getContentAsString()).assertThat().asList().isEmpty();
  }

  @Test
  @WithMockUser
  public void shouldNotUpdateNonExistentFinance() throws Exception {
    // Given
    long userId = 1;
    long financeId = 33;
    Finance finance =
        new Finance(
            Optional.empty(),
            1L,
            1L,
            1L,
            1,
            1,
            200.5f,
            Instant.parse("2023-01-01T00:00:00Z"),
            "Washing machine repair");
    doThrow(FinanceNotFoundException.class).when(financeService).update(finance);

    // When/Then
    mockMvc
        .perform(
            put(String.format("/%s/finance/%s", userId, financeId))
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(financeJacksonTester.write(finance).getJson()))
        .andExpect(status().isNotFound());
    verify(financeService).update(finance);
  }

  @Test
  @WithMockUser
  public void shouldNotDeleteNonExistentFinance() throws Exception {
    // Given
    long userId = 1;
    long financeId = 1;
    doThrow(FinanceNotFoundException.class).when(financeService).deleteById(financeId);

    // When/Then
    mockMvc
        .perform(delete(String.format("/%s/finance/%s", userId, financeId)).with(csrf()))
        .andExpect(status().isNotFound());
    verify(financeService).deleteById(financeId);
  }
}
