package com.inzynierka2k24.apiserver.web.controller;

import com.inzynierka2k24.apiserver.service.FinanceSourceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FinanceSourceController.class)
class FinanceSourceControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private FinanceSourceService financeSourceService;

  @BeforeEach
  void setUp() {
    Map<String, List<String>> mockResponse = Map.of(
        "RESERVATION", List.of("BOOKING", "AIRBNB"),
        "RENOVATION", List.of("CLEANING", "REPAIR")
    );

    when(financeSourceService.getEventsWithSources()).thenReturn(mockResponse);
  }

  @Test
  @WithMockUser
  void getEventsWithSourcesShouldReturnCorrectData() throws Exception {
    mockMvc.perform(get("/finance-source"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.RESERVATION").isArray())
        .andExpect(jsonPath("$.RESERVATION[0]").value("BOOKING"))
        .andExpect(jsonPath("$.RENOVATION").isArray())
        .andExpect(jsonPath("$.RENOVATION[0]").value("CLEANING"));
  }
}
