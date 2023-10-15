package com.inzynierka2k24.apiserver.web.controller;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.inzynierka2k24.apiserver.exception.user.UserAlreadyExistsException;
import com.inzynierka2k24.apiserver.exception.user.UserNotFoundException;
import com.inzynierka2k24.apiserver.model.User;
import com.inzynierka2k24.apiserver.service.UserService;
import com.inzynierka2k24.apiserver.web.request.AuthRequest;
import com.inzynierka2k24.apiserver.web.request.EditUserRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserController.class)
@AutoConfigureJsonTesters
public class UserControllerTest {

  @Autowired private MockMvc mockMvc;
  @Autowired JacksonTester<AuthRequest> authRequestJacksonTester;
  @Autowired JacksonTester<EditUserRequest> editUserRequestJacksonTester;

  @MockBean UserService userService;

  @Test
  @WithMockUser // TODO Find a cause why it doesn't work without this
  public void shouldRegisterNewUserSuccessfully() throws Exception {
    // Given
    AuthRequest request = new AuthRequest("test@example.com", "password");
    User user = new User("test@example.com", "password");
    doNothing().when(userService).register(user);

    // When/Then
    mockMvc
        .perform(
            post("/register")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(authRequestJacksonTester.write(request).getJson()))
        .andExpect(status().isCreated())
        .andExpect(content().string(containsString("User registered successfully")));
    verify(userService).register(user);
  }

  @Test
  @WithMockUser()
  public void shouldUpdateExistingUserSuccessfully() throws Exception {
    // Given
    long userId = 1L;
    EditUserRequest request = new EditUserRequest("test@example.com", "password");
    User user = new User(userId, "test@example.com", "password");
    doNothing().when(userService).update(any());

    // When/Then
    mockMvc
        .perform(
            put(String.format("/user/%s/edit", userId))
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(editUserRequestJacksonTester.write(request).getJson()))
        .andExpect(status().isOk())
        .andExpect(content().string("User updated successfully"));
    verify(userService).update(user);
  }

  @Test
  @WithMockUser()
  public void shouldDeleteExistingUserSuccessfully() throws Exception {
    // Given
    long userId = 1L;
    doNothing().when(userService).deleteById(userId);

    // When/Then
    mockMvc
        .perform(delete(String.format("/user/%s/remove", userId)).with(csrf()))
        .andExpect(status().isOk())
        .andExpect(content().string("User deleted successfully"));
    verify(userService).deleteById(userId);
  }

  @Test
  @WithMockUser // TODO Same as above
  public void shouldNotRegisterUserWithExistingEmail() throws Exception {
    // Given
    AuthRequest request = new AuthRequest("test@example.com", "password");
    User user = new User("test@example.com", "password");
    doThrow(UserAlreadyExistsException.class).when(userService).register(user);

    // When/Then
    mockMvc
        .perform(
            post("/register")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(authRequestJacksonTester.write(request).getJson()))
        .andExpect(status().isBadRequest());
    verify(userService).register(user);
  }

  @Test
  @WithMockUser
  public void shouldNotUpdateNonExistingUser() throws Exception {
    // Given
    long userId = 1L;
    EditUserRequest request = new EditUserRequest("test@example.com", "password");
    User user = new User(userId, "test@example.com", "password");
    doThrow(UserNotFoundException.class).when(userService).update(user);

    // When/Then
    mockMvc
        .perform(
            put(String.format("/user/%s/edit", userId))
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(editUserRequestJacksonTester.write(request).getJson()))
        .andExpect(status().isNotFound());
    verify(userService).update(user);
  }

  @Test
  @WithMockUser
  public void shouldNotDeleteNonExistingUser() throws Exception {
    // Given
    long userId = 1L;
    doThrow(UserNotFoundException.class).when(userService).deleteById(userId);

    // When/Then
    mockMvc
        .perform(delete(String.format("/user/%s/remove", userId)).with(csrf()))
        .andExpect(status().isNotFound());
    verify(userService).deleteById(userId);
  }

  @Test
  @WithMockUser
  public void shouldNotRegisterUserWithInvalidEmail() throws Exception {
    // Given
    AuthRequest request = new AuthRequest("invalid_email", "password");

    // When/Then
    mockMvc
        .perform(
            post("/register")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(authRequestJacksonTester.write(request).getJson()))
        .andExpect(status().isBadRequest());
    verify(userService, never()).register(any(User.class));
  }

  @Test
  @WithMockUser
  public void shouldNotRegisterUserWithShortPassword() throws Exception {
    // Given
    AuthRequest request = new AuthRequest("test@example.com", "pass");

    // When/Then
    mockMvc
        .perform(
            post("/register")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(authRequestJacksonTester.write(request).getJson()))
        .andExpect(status().isBadRequest());
    verify(userService, never()).register(any(User.class));
  }

  @Test
  @WithMockUser
  public void shouldNotUpdateUserWithInvalidEmail() throws Exception {
    // Given
    long userId = 1L;
    EditUserRequest request = new EditUserRequest("invalid_email", "password");

    // When/Then
    mockMvc
        .perform(
            put(String.format("/user/%s/edit", userId))
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(editUserRequestJacksonTester.write(request).getJson()))
        .andExpect(status().isBadRequest());
    verify(userService, never()).update(any(User.class));
  }

  @Test
  @WithMockUser
  public void shouldNotUpdateUserWithShortPassword() throws Exception {
    // Given
    long userId = 1L;
    EditUserRequest request = new EditUserRequest("test@example.com", "pass");

    // When/Then
    mockMvc
        .perform(
            put(String.format("/user/%s/edit", userId))
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(editUserRequestJacksonTester.write(request).getJson()))
        .andExpect(status().isBadRequest());
    verify(userService, never()).update(any(User.class));
  }

  @TestConfiguration
  static class TestConf {

    @Bean
    @Primary
    public PasswordEncoder passwordEncoder() {
      return NoOpPasswordEncoder.getInstance();
    }
  }
}
