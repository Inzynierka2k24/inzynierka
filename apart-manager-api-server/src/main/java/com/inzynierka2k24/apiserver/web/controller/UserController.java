package com.inzynierka2k24.apiserver.web.controller;

import com.inzynierka2k24.apiserver.exception.user.UserAlreadyExistsException;
import com.inzynierka2k24.apiserver.exception.user.UserNotFoundException;
import com.inzynierka2k24.apiserver.model.User;
import com.inzynierka2k24.apiserver.service.AuthorizationService;
import com.inzynierka2k24.apiserver.service.UserService;
import com.inzynierka2k24.apiserver.web.config.JwtAuthConverter;
import com.inzynierka2k24.apiserver.web.dto.UserDTO;
import com.inzynierka2k24.apiserver.web.request.AuthRequest;
import com.inzynierka2k24.apiserver.web.request.EditUserRequest;
import com.inzynierka2k24.apiserver.web.request.RegisterRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
public class UserController {

  private final AuthorizationService authorizationService;
  private final UserService userService;

  @PostMapping("/login")
  public ResponseEntity<String> login(@Valid @RequestBody AuthRequest request) {
    String token = authorizationService.getToken(request.login(), request.password());
    log.info(token);
    return token != null
        ? ResponseEntity.ok(token)
        : ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
  }

  @PostMapping("/register")
  public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest request)
      throws UserAlreadyExistsException {
    authorizationService.register(request.emailAddress(), request.login(), request.password());
    userService.register(new User(request.login(), request.emailAddress()));
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @GetMapping("/user/details")
  public ResponseEntity<UserDTO> getDetails(HttpServletRequest request) {
    var token = request.getHeader("Authorization");
    if (token == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    String username = JwtAuthConverter.getLoginFromJWT(token);
    User u = userService.getUser(username);
    UserDTO dto = new UserDTO(u.id().get(), u.login(), u.emailAddress());
    return ResponseEntity.ok(dto);
  }

  @PutMapping("/user/{userId}/edit")
  public ResponseEntity<String> edit(
      @RequestHeader(HttpHeaders.AUTHORIZATION) String authToken,
      @PathVariable long userId,
      @Valid @RequestBody EditUserRequest request)
      throws UserNotFoundException {
    authorizationService.edit(authToken, request);
    userService.update(new User(userId, request.username(), request.emailAddress()));
    return ResponseEntity.ok("User updated successfully");
  }

  @DeleteMapping("/user/{userId}/remove")
  public ResponseEntity<String> delete(@PathVariable long userId) throws UserNotFoundException {
    userService.deleteById(userId);
    return ResponseEntity.ok("User deleted successfully");
  }
}
