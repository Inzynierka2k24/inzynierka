package com.inzynierka2k24.apiserver.web.controller;

import static com.inzynierka2k24.apiserver.model.ServiceType.AIRBNB;
import static com.inzynierka2k24.apiserver.model.ServiceType.BOOKING;
import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.inzynierka2k24.apiserver.exception.account.AccountNotFoundException;
import com.inzynierka2k24.apiserver.model.ExternalAccount;
import com.inzynierka2k24.apiserver.service.ExternalAccountService;
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

@WebMvcTest(ExternalAccountController.class)
@AutoConfigureJsonTesters
public class ExternalAccountControllerTest {

  @Autowired private MockMvc mockMvc;
  @Autowired JacksonTester<ExternalAccount> accountJacksonTester;
  @Autowired JacksonTester<List<ExternalAccount>> accountListJacksonTester;
  @MockBean ExternalAccountService accountService;

  @Test
  @WithMockUser
  public void shouldGetAllAccountsForGivenUserId() throws Exception {
    // Given
    long userId = 1;
    List<ExternalAccount> expectedAccounts =
        List.of(
            new ExternalAccount(Optional.of(1L), "test", "test", AIRBNB),
            new ExternalAccount(Optional.of(2L), "test2", "test2", BOOKING));
    when(accountService.getAll(userId)).thenReturn(expectedAccounts);

    // When/Then
    var response =
        mockMvc
            .perform(
                get(String.format("/%s/external/account", userId))
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse();
    verify(accountService).getAll(userId);
    accountListJacksonTester
        .parse(response.getContentAsString())
        .assertThat()
        .asList()
        .hasSize(2)
        .containsAll(expectedAccounts);
  }

  @Test
  @WithMockUser
  public void shouldGetAccountForGivenUserIdAndAccountId() throws Exception {
    // Given
    long userId = 1;
    long accountId = 1;
    ExternalAccount expectedAccount = new ExternalAccount(Optional.of(1L), "test", "test", AIRBNB);
    when(accountService.getById(userId, accountId)).thenReturn(expectedAccount);

    // When/Then
    var response =
        mockMvc
            .perform(
                get(String.format("/%s/external/account/%s", userId, accountId))
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse();
    verify(accountService).getById(userId, accountId);
    assertEquals(
        expectedAccount, accountJacksonTester.parse(response.getContentAsString()).getObject());
  }

  @Test
  @WithMockUser
  public void shouldAddAccountForGivenUserId() throws Exception {
    // Given
    long userId = 1;
    ExternalAccount account = new ExternalAccount(Optional.of(1L), "test", "test", AIRBNB);
    doNothing().when(accountService).add(userId, account);

    // When/Then
    mockMvc
        .perform(
            post(String.format("/%s/external/account", userId))
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(accountJacksonTester.write(account).getJson()))
        .andExpect(status().isCreated())
        .andExpect(content().string("Account created successfully"));
    verify(accountService).add(userId, account);
  }

  @Test
  @WithMockUser
  public void shouldEditExistingAccountForGivenUserId() throws Exception {
    // Given
    long userId = 1;
    ExternalAccount account = new ExternalAccount(Optional.of(1L), "test", "test", AIRBNB);
    doNothing().when(accountService).update(userId, account);

    // When/Then
    mockMvc
        .perform(
            put(String.format("/%s/external/account", userId))
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(accountJacksonTester.write(account).getJson()))
        .andExpect(status().isOk())
        .andExpect(content().string("Account edited successfully"));
    verify(accountService).update(userId, account);
  }

  @Test
  @WithMockUser
  public void shouldDeleteExistingAccountForGivenUserIdAndAccountId() throws Exception {
    // Given
    long userId = 1;
    long accountId = 1;
    doNothing().when(accountService).deleteById(userId, accountId);

    // When/Then
    mockMvc
        .perform(delete(String.format("/%s/external/account/%s", userId, accountId)).with(csrf()))
        .andExpect(status().isOk())
        .andExpect(content().string("Account deleted successfully"));
    verify(accountService).deleteById(userId, accountId);
  }

  @Test
  @WithMockUser
  public void shouldGetNoAccountsForUserWithNoAccounts() throws Exception {
    // Given
    long userId = 1;
    List<ExternalAccount> expectedAccounts = emptyList();
    when(accountService.getAll(userId)).thenReturn(expectedAccounts);

    // When/Then
    var response =
        mockMvc
            .perform(get(String.format("/%s/external/account", userId)))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse();
    verify(accountService).getAll(userId);
    accountListJacksonTester.parse(response.getContentAsString()).assertThat().asList().isEmpty();
  }

  @Test
  @WithMockUser
  public void shouldThrowWhenGetNonExistentAccount() throws Exception {
    // Given
    long userId = 1;
    long accountId = 2;
    doThrow(AccountNotFoundException.class).when(accountService).getById(userId, accountId);

    // When/Then
    mockMvc
        .perform(get(String.format("/%s/external/account/%s", userId, accountId)))
        .andExpect(status().isNotFound());
    verify(accountService).getById(userId, accountId);
  }

  @Test
  @WithMockUser
  public void shouldNotUpdateNonExistentAccount() throws Exception {
    // Given
    long userId = 1;
    ExternalAccount account = new ExternalAccount(Optional.of(1L), "test", "test", AIRBNB);
    doThrow(AccountNotFoundException.class).when(accountService).update(userId, account);

    // When/Then
    mockMvc
        .perform(
            put(String.format("/%s/external/account", userId))
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(accountJacksonTester.write(account).getJson()))
        .andExpect(status().isNotFound());
    verify(accountService).update(userId, account);
  }

  @Test
  @WithMockUser
  public void shouldNotDeleteNonExistentAccount() throws Exception {
    // Given
    long userId = 1;
    long accountId = 1;
    doThrow(AccountNotFoundException.class).when(accountService).deleteById(userId, accountId);

    // When/Then
    mockMvc
        .perform(delete(String.format("/%s/external/account/%s", userId, accountId)).with(csrf()))
        .andExpect(status().isNotFound());
    verify(accountService).deleteById(userId, accountId);
  }
}
