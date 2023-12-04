package com.inzynierka2k24.apiserver.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

import com.inzynierka2k24.apiserver.exception.user.InvalidCredentialsException;
import com.inzynierka2k24.apiserver.exception.user.UserAlreadyExistsException;
import com.inzynierka2k24.apiserver.exception.user.UserNotFoundException;
import com.inzynierka2k24.apiserver.web.response.KeycloakTokenResponse;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.*;
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
    ReflectionTestUtils.setField(authorizationService, "clientId", CLIENT_ID);
    ReflectionTestUtils.setField(authorizationService, "adminLogin", ADMIN_USERNAME);
    ReflectionTestUtils.setField(authorizationService, "adminPassword", ADMIN_PASSWORD);
  }

  @Test
  void shouldReturnToken() {
    // given
    var request = getRequestEntity();
    var response = getTokenResponseEntity();

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
    var tokenResponse = getTokenResponseEntity();
    given(restTemplate.postForEntity(eq(TOKEN_ENDPOINT), any(), eq(KeycloakTokenResponse.class)))
        .willReturn(tokenResponse);

    // when then
    assertThatCode(() -> authorizationService.register("emailAddress", USERNAME, PASSWORD))
        .doesNotThrowAnyException();
  }

  @Test
  void register_shouldThrowExceptionWhenUserAlreadyExists() {
    // given
    var tokenResponse = getTokenResponseEntity();
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
    var tokenResponse = getTokenResponseEntity();
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
    var tokenResponse = getTokenResponseEntity();
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
  void shouldGetHeaderWithAdminToken() {
    // given
    var request = getAdminRequestEntity();
    var response = getTokenResponseEntity();

    given(restTemplate.postForEntity(TOKEN_ENDPOINT, request, KeycloakTokenResponse.class))
        .willReturn(response);

    // when
    final var result = authorizationService.headersWithAdminToken();

    // then
    assertThat(result.get("Authorization").get(0)).isEqualTo("Bearer " + TOKEN);
  }

  @Test
  void edit_shouldGetIdForUserByLogin() throws UserNotFoundException {
    // given

    HttpEntity<MultiValueMap<String, String>> tokenRequest = getAdminRequestEntity();
    var response = getTokenResponseEntity();
    given(restTemplate.postForEntity(TOKEN_ENDPOINT, tokenRequest, KeycloakTokenResponse.class))
        .willReturn(response);

    Map<String, String> params = new HashMap<>();
    params.put("username", USERNAME);
    HttpEntity<Map<String, Object>> requestEntity =
        new HttpEntity<>(authorizationService.headersWithAdminToken());

    AuthorizationService.UserRepresentation userRepresentation =
        new AuthorizationService.UserRepresentation();
    userRepresentation.id = "id";
    userRepresentation.username = USERNAME;
    ResponseEntity<AuthorizationService.UserRepresentation[]> responseEntityWithBody =
        getResponseEntityWithBody(
            new AuthorizationService.UserRepresentation[] {userRepresentation});

    given(
            restTemplate.exchange(
                USER_ENDPOINT,
                HttpMethod.GET,
                requestEntity,
                AuthorizationService.UserRepresentation[].class,
                params))
        .willReturn(responseEntityWithBody);

    // when
    final var result = authorizationService.getUserIdByLogin(USERNAME);

    // then
    assertThat(result).isEqualTo("id");
  }

  @Test
  void shouldSuccessfullyEdit() {
    // given
    var tokenResponse = getTokenResponseEntity();
    given(restTemplate.postForEntity(eq(TOKEN_ENDPOINT), any(), eq(KeycloakTokenResponse.class)))
        .willReturn(tokenResponse);
    AuthorizationService.UserRepresentation userRepresentation =
        new AuthorizationService.UserRepresentation();
    userRepresentation.id = "id";
    userRepresentation.username = USERNAME;
    var userRepresentationResponseEntity =
        getResponseEntityWithBody(
            new AuthorizationService.UserRepresentation[] {userRepresentation});
    Map<String, String> params = new HashMap<>();
    params.put("username", USERNAME);
    given(
            restTemplate.exchange(
                eq(USER_ENDPOINT),
                eq(HttpMethod.GET),
                any(),
                eq(AuthorizationService.UserRepresentation[].class),
                eq(params)))
        .willReturn(userRepresentationResponseEntity);

    Map<String, Object> requestBody = new HashMap<>();
    requestBody.put("username", USERNAME);
    requestBody.put("email", EMAIL_ADDRESS);
    requestBody.put("enabled", true);
    requestBody.put("emailVerified", true);
    Map<String, String> credentials = new HashMap<>();
    credentials.put("type", "password");
    credentials.put("value", PASSWORD);
    requestBody.put("credentials", Collections.singletonList(credentials));

    HttpEntity<Map<String, Object>> requestEntity =
        new HttpEntity<>(requestBody, authorizationService.headersWithAdminToken());

    given(
            restTemplate.exchange(
                eq(USER_ENDPOINT + "id"), eq(HttpMethod.PUT), eq(requestEntity), eq(String.class)))
        .willReturn(getResponseEntityWithBody(""));

    // when then
    assertThatCode(() -> authorizationService.edit(USERNAME, EMAIL_ADDRESS, PASSWORD))
        .doesNotThrowAnyException();
  }

  private ResponseEntity<KeycloakTokenResponse> getTokenResponseEntity() {
    KeycloakTokenResponse tokenResponse = new KeycloakTokenResponse();
    tokenResponse.setAccessToken(TOKEN);
    return new ResponseEntity<>(tokenResponse, HttpStatus.OK);
  }

  private <T> ResponseEntity<T> getResponseEntityWithBody(T body) {
    return new ResponseEntity<>(body, HttpStatus.OK);
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
    formData.add("scope", "openid");

    return new HttpEntity<>(formData, headers);
  }

  private HttpEntity<MultiValueMap<String, String>> getAdminRequestEntity() {

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

    MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
    formData.add("grant_type", "password");
    formData.add("client_id", CLIENT_ID);
    formData.add("username", AuthorizationServiceTest.ADMIN_USERNAME);
    formData.add("password", AuthorizationServiceTest.ADMIN_PASSWORD);
    formData.add("scope", "openid");

    return new HttpEntity<>(formData, headers);
  }
}
