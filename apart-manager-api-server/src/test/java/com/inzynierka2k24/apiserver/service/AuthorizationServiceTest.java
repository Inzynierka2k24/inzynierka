package com.inzynierka2k24.apiserver.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

import com.inzynierka2k24.apiserver.exception.user.InvalidCredentialsException;
import com.inzynierka2k24.apiserver.exception.user.UserAlreadyExistsException;
import com.inzynierka2k24.apiserver.web.request.EditUserRequest;
import com.inzynierka2k24.apiserver.web.response.KeycloakTokenResponse;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
class AuthorizationServiceTest {

  private static final String TOKEN_ENDPOINT = "https://token-endpoint.com";
  private static final String USER_ENDPOINT = "https://user-endpoint.com";
  private static final String USER_DETAILS_ENDPOINT = "https://user-details-endpoint.com";
  private static final String CLIENT_ID = "client-id";
  private static final String TOKEN = "testToken";
  private static final String USERNAME = "username";
  private static final String EMAIL_ADDRESS = "test@example.com";
  private static final String PASSWORD = "password";
  private static final String ADMIN_USERNAME = "admin_username";
  private static final String ADMIN_PASSWORD = "admin_password";

  @Mock RestTemplate restTemplate;

  @InjectMocks AuthorizationService authorizationService;

  @BeforeEach
  void setUp() {
    ReflectionTestUtils.setField(authorizationService, "tokenEndpoint", TOKEN_ENDPOINT);
    ReflectionTestUtils.setField(authorizationService, "userEndpoint", USER_ENDPOINT);
    ReflectionTestUtils.setField(
        authorizationService, "userDetailsEndpoint", USER_DETAILS_ENDPOINT);
    ReflectionTestUtils.setField(authorizationService, "clientId", CLIENT_ID);
    ReflectionTestUtils.setField(authorizationService, "adminLogin", ADMIN_USERNAME);
    ReflectionTestUtils.setField(authorizationService, "adminPassword", ADMIN_PASSWORD);
  }

  @Test
  void shouldReturnToken() {
    // given
    var request = getRequestEntity();
    var response = getResponseEntity();

    given(restTemplate.postForEntity(TOKEN_ENDPOINT, request, KeycloakTokenResponse.class))
        .willReturn(response);

    // when
    final var result = authorizationService.getToken(USERNAME, PASSWORD);

    // then
    assertThat(result).isEqualTo(TOKEN);
  }

  @Test
  void shouldThrowExceptionWhenInvalidCredentialsProvided() {
    // given
    var request = getRequestEntity();

    given(restTemplate.postForEntity(TOKEN_ENDPOINT, request, KeycloakTokenResponse.class))
        .willThrow(HttpClientErrorException.class);

    // when then
    assertThatThrownBy(() -> authorizationService.getToken(USERNAME, PASSWORD))
        .isInstanceOf(InvalidCredentialsException.class);
  }

  @Test
  void shouldSuccessfullyRegisterWhenValidCredentials() {
    // given
    var tokenResponse = getResponseEntity();
    given(restTemplate.postForEntity(eq(TOKEN_ENDPOINT), any(), eq(KeycloakTokenResponse.class)))
        .willReturn(tokenResponse);

    // when then
    assertThatCode(() -> authorizationService.register("emailAddress", USERNAME, PASSWORD))
        .doesNotThrowAnyException();
  }

  @Test
  void register_shouldThrowExceptionWhenUserAlreadyExists() {
    // given
    var tokenResponse = getResponseEntity();
    given(restTemplate.postForEntity(eq(TOKEN_ENDPOINT), any(), eq(KeycloakTokenResponse.class)))
        .willReturn(tokenResponse);
    given(restTemplate.exchange(eq(USER_ENDPOINT), eq(HttpMethod.POST), any(), eq(String.class)))
        .willThrow(new HttpClientErrorException(HttpStatus.CONFLICT));

    // when then
    assertThatThrownBy(() -> authorizationService.register(EMAIL_ADDRESS, USERNAME, PASSWORD))
        .isInstanceOf(UserAlreadyExistsException.class);
  }

  @Test
  void register_shouldThrowExceptionWhenClientError() {
    // given
    var tokenResponse = getResponseEntity();
    given(restTemplate.postForEntity(eq(TOKEN_ENDPOINT), any(), eq(KeycloakTokenResponse.class)))
        .willReturn(tokenResponse);
    given(restTemplate.exchange(eq(USER_ENDPOINT), eq(HttpMethod.POST), any(), eq(String.class)))
        .willThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

    // when then
    assertThatThrownBy(() -> authorizationService.register(EMAIL_ADDRESS, USERNAME, PASSWORD))
        .isInstanceOf(RuntimeException.class)
        .hasMessage("User registration failed: " + HttpStatus.BAD_REQUEST);
  }

  @Test
  void register_shouldThrowExceptionWhenServerError() {
    // given
    var tokenResponse = getResponseEntity();
    given(restTemplate.postForEntity(eq(TOKEN_ENDPOINT), any(), eq(KeycloakTokenResponse.class)))
        .willReturn(tokenResponse);
    given(restTemplate.exchange(eq(USER_ENDPOINT), eq(HttpMethod.POST), any(), eq(String.class)))
        .willThrow(HttpServerErrorException.class);

    // when then
    assertThatThrownBy(() -> authorizationService.register(EMAIL_ADDRESS, USERNAME, PASSWORD))
        .isInstanceOf(RuntimeException.class)
        .hasMessage("Server error during registration");
  }

  @Test
  void shouldSuccessfullyEdit() {
    // given
    var editUserRequest = getEditUserRequest();

    var userIdRequest = getUserIdRequestEntity();
    var userIdResponse = new ResponseEntity<Map>(Map.of("sub", "id"), HttpStatus.OK);
    given(restTemplate.exchange(userIdRequest, Map.class)).willReturn(userIdResponse);

    var tokenResponse = getResponseEntity();
    given(restTemplate.postForEntity(eq(TOKEN_ENDPOINT), any(), eq(KeycloakTokenResponse.class)))
        .willReturn(tokenResponse);

    // when then
    assertThatCode(() -> authorizationService.edit(TOKEN, editUserRequest))
        .doesNotThrowAnyException();
  }

  @Test
  void edit_shouldThrowExceptionWhenClientError() {
    // given
    var editUserRequest = getEditUserRequest();

    var userIdRequest = getUserIdRequestEntity();
    given(restTemplate.exchange(userIdRequest, Map.class))
        .willThrow(HttpClientErrorException.class);

    // when then
    assertThatThrownBy(() -> authorizationService.edit(TOKEN, editUserRequest))
        .isInstanceOf(RuntimeException.class);
  }

  @Test
  void edit_shouldThrowExceptionWhenServerError() {
    // given
    var editUserRequest = getEditUserRequest();

    var userIdRequest = getUserIdRequestEntity();
    var userIdResponse = new ResponseEntity<Map>(Map.of("sub", "id"), HttpStatus.OK);
    given(restTemplate.exchange(userIdRequest, Map.class)).willReturn(userIdResponse);

    var tokenResponse = getResponseEntity();
    given(restTemplate.postForEntity(eq(TOKEN_ENDPOINT), any(), eq(KeycloakTokenResponse.class)))
        .willReturn(tokenResponse);

    given(
            restTemplate.exchange(
                eq(USER_ENDPOINT + "id"), eq(HttpMethod.PUT), any(), eq(String.class)))
        .willThrow(HttpServerErrorException.class);

    // when then
    assertThatThrownBy(() -> authorizationService.edit(TOKEN, editUserRequest))
        .isInstanceOf(RuntimeException.class);
  }

  private EditUserRequest getEditUserRequest() {
    return new EditUserRequest(USERNAME, EMAIL_ADDRESS, PASSWORD);
  }

  private RequestEntity<Void> getUserIdRequestEntity() {
    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + TOKEN);

    return RequestEntity.get(USER_DETAILS_ENDPOINT)
        .accept(MediaType.APPLICATION_JSON)
        .headers(headers)
        .build();
  }

  private ResponseEntity<KeycloakTokenResponse> getResponseEntity() {
    KeycloakTokenResponse tokenResponse = new KeycloakTokenResponse();
    tokenResponse.setAccessToken(TOKEN);
    return new ResponseEntity<>(tokenResponse, HttpStatus.OK);
  }

  private HttpEntity<MultiValueMap<String, String>> getRequestEntity() {

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

    MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
    formData.add("grant_type", "password");
    formData.add("client_id", CLIENT_ID);
    formData.add("username", AuthorizationServiceTest.USERNAME);
    formData.add("password", AuthorizationServiceTest.PASSWORD);

    return new HttpEntity<>(formData, headers);
  }
}
