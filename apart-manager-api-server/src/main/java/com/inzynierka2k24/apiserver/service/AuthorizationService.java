package com.inzynierka2k24.apiserver.service;

import com.inzynierka2k24.apiserver.exception.user.InvalidCredentialsException;
import com.inzynierka2k24.apiserver.exception.user.UserAlreadyExistsException;
import com.inzynierka2k24.apiserver.web.response.KeycloakTokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthorizationService {

  private static final String GRANT_TYPE = "grant_type";
  private static final String CLIENT_ID = "client_id";
  private static final String USERNAME = "username";
  private static final String PASSWORD = "password";

  private final RestTemplate restTemplate;

  @Value("${keycloak.client-id}")
  private String clientId;

  @Value("${keycloak.credentials.login}")
  private String adminLogin;

  @Value("${keycloak.credentials.password}")
  private String adminPassword;

  @Value("${keycloak.token-endpoint}")
  private String tokenEndpoint;

  @Value("${keycloak.create-user-endpoint}")
  private String createUserEndpoint;

  @Value("${keycloak.edit-user-endpoint}")
  private String editUserEndpoint;

  private String deleteUserEndpoint;

  public AuthorizationService(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  public String getToken(String username, String password) {
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

    MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
    formData.add(GRANT_TYPE, "password");
    formData.add(CLIENT_ID, clientId);
    formData.add(USERNAME, username);
    formData.add(PASSWORD, password);

    HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(formData, headers);

    try {
      ResponseEntity<KeycloakTokenResponse> responseEntity =
          restTemplate.postForEntity(tokenEndpoint, requestEntity, KeycloakTokenResponse.class);

      return responseEntity.getBody().getAccessToken();
    } catch (HttpClientErrorException e) {
      throw new InvalidCredentialsException(e);
    }
  }

  public void register(String emailAddress, String username, String password) {
    // token with admin privileges
    String token = getToken(adminLogin, adminPassword);

    // headers
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.set("Authorization", "Bearer " + token);

    // request body
    Map<String, Object> requestBody = new HashMap<>();
    requestBody.put("username", username);
    requestBody.put("email", emailAddress);
    requestBody.put("enabled", true);
    requestBody.put("emailVerified", true);
    Map<String, String> credentials = new HashMap<>();
    credentials.put("type", "password");
    credentials.put("value", password);
    requestBody.put("credentials", Collections.singletonList(credentials));

    HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

    // request and error status handling
    try {
      restTemplate.exchange(createUserEndpoint, HttpMethod.POST, requestEntity, String.class);
    } catch (HttpClientErrorException e) {
      if (HttpStatus.CONFLICT.equals(e.getStatusCode())) {
        throw new UserAlreadyExistsException(e);
      }
      throw new RuntimeException("User registration failed: " + e.getStatusCode());
    } catch (HttpServerErrorException e) {
      throw new RuntimeException("Server error during registration", e);
    }
  }

  public void edit(String username, String emailAddress, String password){
    // request body
    Map<String, Object> requestBody = new HashMap<>();
    requestBody.put("username", username);
    requestBody.put("email", emailAddress);
    requestBody.put("enabled", true);
    requestBody.put("emailVerified", true);
    Map<String, String> credentials = new HashMap<>();
    credentials.put("type", "password");
    credentials.put("value", password);
    requestBody.put("credentials", Collections.singletonList(credentials));

    HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headersWithAdminToken());

    try{
      restTemplate.exchange(editUserEndpoint, HttpMethod.PUT, requestEntity, String.class);
    }catch (HttpServerErrorException e){
      throw new RuntimeException("Server error during user edition");
    }
  }

  private HttpHeaders headersWithAdminToken(){
    String token = getToken(adminLogin,adminPassword);
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.set("Authorization", "Bearer " + token);
    return headers;
  }
}
