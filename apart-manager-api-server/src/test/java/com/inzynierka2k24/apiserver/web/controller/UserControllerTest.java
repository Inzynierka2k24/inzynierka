package com.inzynierka2k24.apiserver.web.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.inzynierka2k24.apiserver.exception.user.InvalidCredentialsException;
import com.inzynierka2k24.apiserver.exception.user.UserAlreadyExistsException;
import com.inzynierka2k24.apiserver.service.AuthorizationService;
import com.inzynierka2k24.apiserver.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {

  @MockBean AuthorizationService authorizationService;
  @MockBean UserService userService;
  @Autowired private MockMvc mockMvc;

  @Test
  @WithMockUser
  void shouldReturnJwtToken() throws Exception {
    // given
    String login = "user";
    String password = "password";
    String token = "token123";

    when(authorizationService.getToken(login, password)).thenReturn(token);

    // when then
    mockMvc
        .perform(
            post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"login\":\"" + login + "\", \"password\":\"" + password + "\"}"))
        .andExpect(status().isOk())
        .andExpect(content().string(token));

    verify(authorizationService).getToken(login, password);
  }

  @Test
  void shouldReturnBadRequestWhenInvalidCredentials() throws Exception {
    // given
    String login = "user";
    String password = "invalid_password";
    when(authorizationService.getToken(login, password))
        .thenThrow(InvalidCredentialsException.class);

    // when then
    mockMvc
        .perform(
            post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"login\":\"" + login + "\", \"password\":\"" + password + "\"}"))
        .andExpect(status().isBadRequest());

    verify(authorizationService).getToken(login, password);
  }

  @Test
  void shouldRegisterUser() throws Exception {
    // given
    String login = "user";
    String email = "email@example.com";
    String password = "password";

    // when then
    mockMvc
        .perform(
            post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    "{ \"login\":\""
                        + login
                        + "\", \"emailAddress\":\""
                        + email
                        + "\", \"password\":\""
                        + password
                        + "\"}"))
        .andExpect(status().isOk());

    verify(authorizationService).register(email, login, password);
  }

  @Test
  void shouldReturnConflictWhenCredentialsExists() throws Exception {
    // given
    String login = "username_exists";
    String email = "email@example.com";
    String password = "password";

    doThrow(new UserAlreadyExistsException())
        .when(authorizationService)
        .register(email, login, password);

    // when then
    mockMvc
        .perform(
            post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    "{ \"login\":\""
                        + login
                        + "\", \"emailAddress\":\""
                        + email
                        + "\", \"password\":\""
                        + password
                        + "\"}"))
        .andExpect(status().isConflict());

    verify(authorizationService).register(email, login, password);
  }

  @Test
  void shouldReturnBadRequestWhenException() throws Exception {
    // given
    String login = "username";
    String email = "email@example.com";
    String password = "password";

    doThrow(new RuntimeException()).when(authorizationService).register(email, login, password);

    // when then
    mockMvc
        .perform(
            post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    "{ \"login\":\""
                        + login
                        + "\", \"emailAddress\":\""
                        + email
                        + "\", \"password\":\""
                        + password
                        + "\"}"))
        .andExpect(status().isBadRequest());

    verify(authorizationService).register(email, login, password);
  }

  //  @Test
  //  @WithMockUser()
  //  public void shouldUpdateExistingUserSuccessfully() throws Exception {
  //    // Given
  //    long userId = 1L;
  //    EditUserRequest request = new EditUserRequest("test@example.com", "password");
  //    User user = new User(userId, "test@example.com", "password");
  //    doNothing().when(userService).update(any());
  //
  //    // When/Then
  //    mockMvc
  //        .perform(
  //            put(String.format("/user/%s/edit", userId))
  //                .with(csrf())
  //                .contentType(MediaType.APPLICATION_JSON)
  //                .content(editUserRequestJacksonTester.write(request).getJson()))
  //        .andExpect(status().isOk())
  //        .andExpect(content().string("User updated successfully"));
  //    verify(userService).update(user);
  //  }
  //
  //  @Test
  //  @WithMockUser()
  //  public void shouldDeleteExistingUserSuccessfully() throws Exception {
  //    // Given
  //    long userId = 1L;
  //    doNothing().when(userService).deleteById(userId);
  //
  //    // When/Then
  //    mockMvc
  //        .perform(delete(String.format("/user/%s/remove", userId)).with(csrf()))
  //        .andExpect(status().isOk())
  //        .andExpect(content().string("User deleted successfully"));
  //    verify(userService).deleteById(userId);
  //  }
  //
  //  @Test
  //  @WithMockUser // TODO Same as above
  //  public void shouldNotRegisterUserWithExistingEmail() throws Exception {
  //    // Given
  //    AuthRequest request = new AuthRequest("test@example.com", "password");
  //    User user = new User("test@example.com", "password");
  //    doThrow(UserAlreadyExistsException.class).when(userService).register(user);
  //
  //    // When/Then
  //    mockMvc
  //        .perform(
  //            post("/register")
  //                .with(csrf())
  //                .contentType(MediaType.APPLICATION_JSON)
  //                .content(authRequestJacksonTester.write(request).getJson()))
  //        .andExpect(status().isBadRequest());
  //    verify(userService).register(user);
  //  }
  //
  //  @Test
  //  @WithMockUser
  //  public void shouldNotUpdateNonExistingUser() throws Exception {
  //    // Given
  //    long userId = 1L;
  //    EditUserRequest request = new EditUserRequest("test@example.com", "password");
  //    User user = new User(userId, "test@example.com", "password");
  //    doThrow(UserNotFoundException.class).when(userService).update(user);
  //
  //    // When/Then
  //    mockMvc
  //        .perform(
  //            put(String.format("/user/%s/edit", userId))
  //                .with(csrf())
  //                .contentType(MediaType.APPLICATION_JSON)
  //                .content(editUserRequestJacksonTester.write(request).getJson()))
  //        .andExpect(status().isNotFound());
  //    verify(userService).update(user);
  //  }
  //
  //  @Test
  //  @WithMockUser
  //  public void shouldNotDeleteNonExistingUser() throws Exception {
  //    // Given
  //    long userId = 1L;
  //    doThrow(UserNotFoundException.class).when(userService).deleteById(userId);
  //
  //    // When/Then
  //    mockMvc
  //        .perform(delete(String.format("/user/%s/remove", userId)).with(csrf()))
  //        .andExpect(status().isNotFound());
  //    verify(userService).deleteById(userId);
  //  }
  //
  //  @Test
  //  @WithMockUser
  //  public void shouldNotRegisterUserWithInvalidEmail() throws Exception {
  //    // Given
  //    AuthRequest request = new AuthRequest("invalid_email", "password");
  //
  //    // When/Then
  //    mockMvc
  //        .perform(
  //            post("/register")
  //                .with(csrf())
  //                .contentType(MediaType.APPLICATION_JSON)
  //                .content(authRequestJacksonTester.write(request).getJson()))
  //        .andExpect(status().isBadRequest());
  //    verify(userService, never()).register(any(User.class));
  //  }
  //
  //  @Test
  //  @WithMockUser
  //  public void shouldNotRegisterUserWithShortPassword() throws Exception {
  //    // Given
  //    AuthRequest request = new AuthRequest("test@example.com", "pass");
  //
  //    // When/Then
  //    mockMvc
  //        .perform(
  //            post("/register")
  //                .with(csrf())
  //                .contentType(MediaType.APPLICATION_JSON)
  //                .content(authRequestJacksonTester.write(request).getJson()))
  //        .andExpect(status().isBadRequest());
  //    verify(userService, never()).register(any(User.class));
  //  }

  //  @Test
  //  @WithMockUser
  //  public void shouldNotUpdateUserWithInvalidEmail() throws Exception {
  //    // Given
  //    long userId = 1L;
  //    EditUserRequest request = new EditUserRequest("invalid_email", "password");
  //
  //    // When/Then
  //    mockMvc
  //        .perform(
  //            put(String.format("/user/%s/edit", userId))
  //                .with(csrf())
  //                .contentType(MediaType.APPLICATION_JSON)
  //                .content(editUserRequestJacksonTester.write(request).getJson()))
  //        .andExpect(status().isBadRequest());
  //    verify(userService, never()).update(any(User.class));
  //  }

  //  @Test
  //  @WithMockUser
  //  public void shouldNotUpdateUserWithShortPassword() throws Exception {
  //    // Given
  //    long userId = 1L;
  //    EditUserRequest request = new EditUserRequest("test@example.com", "pass");
  //
  //    // When/Then
  //    mockMvc
  //        .perform(
  //            put(String.format("/user/%s/edit", userId))
  //                .with(csrf())
  //                .contentType(MediaType.APPLICATION_JSON)
  //                .content(editUserRequestJacksonTester.write(request).getJson()))
  //        .andExpect(status().isBadRequest());
  //    verify(userService, never()).update(any(User.class));
  //  }

}
