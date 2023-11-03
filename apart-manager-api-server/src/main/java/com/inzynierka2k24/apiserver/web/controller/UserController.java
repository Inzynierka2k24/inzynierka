package com.inzynierka2k24.apiserver.web.controller;

import com.inzynierka2k24.apiserver.exception.user.UserAlreadyExistsException;
import com.inzynierka2k24.apiserver.model.User;
import com.inzynierka2k24.apiserver.service.AuthorizationService;
import com.inzynierka2k24.apiserver.service.UserService;
import com.inzynierka2k24.apiserver.web.config.JwtAuthConverter;
import com.inzynierka2k24.apiserver.web.dto.UserDTO;
import com.inzynierka2k24.apiserver.web.request.AuthRequest;
import com.inzynierka2k24.apiserver.web.request.RegisterRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class UserController {

  private final AuthorizationService authorizationService;
  private final UserService userService;

  @PostMapping("/login")
  public ResponseEntity<String> login(@Valid @RequestBody AuthRequest request) {
    String token = authorizationService.getToken(request.login(), request.password());
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
    String username = JwtAuthConverter.getEmailFromJWT(token);
    User u = userService.getUser(username);
    UserDTO dto = new UserDTO(u.emailAddress(), u.login());
    return ResponseEntity.ok(dto);
  }

  // TODO: Implement this using keycloak authentication server
  //  @PutMapping("/user/{userId}/edit")
  //  public ResponseEntity<String> edit(
  //      @PathVariable long userId, @Valid @RequestBody EditUserRequest request)
  //      throws UserNotFoundException {
  //    userService.update(
  //        new User(userId, request.login(), passwordEncoder.encode(request.password())));
  //    return ResponseEntity.ok("User updated successfully");
  //  }
  //
  //  @DeleteMapping("/user/{userId}/remove")
  //  public ResponseEntity<String> delete(@PathVariable long userId) throws UserNotFoundException {
  //    userService.deleteById(userId);
  //    return ResponseEntity.ok("User deleted successfully");
  //  }
}
