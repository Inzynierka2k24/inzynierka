package com.inzynierka2k24.apiserver.web.controller;

import com.inzynierka2k24.apiserver.service.AuthorizationService;
import com.inzynierka2k24.apiserver.web.request.AuthRequest;
import com.inzynierka2k24.apiserver.web.request.RegisterRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

  private final AuthorizationService authorizationService;

  @PostMapping("/login")
  public String login(@RequestBody AuthRequest authRequest) {
    return authorizationService.getToken(authRequest.login(), authRequest.password());
  }

  @PostMapping("/register")
  public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest request) {
    authorizationService.register(request.emailAddress(), request.login(), request.password());
    return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
  }

  // TODO: Implement this using keycloak authentication server
  //  @PutMapping("/user/{userId}/edit")
  //  public ResponseEntity<String> edit(
  //      @PathVariable long userId, @Valid @RequestBody EditUserRequest request)
  //      throws UserNotFoundException {
  //    userService.update(
  //        new User(userId, request.mail(), passwordEncoder.encode(request.password())));
  //    return ResponseEntity.ok("User updated successfully");
  //  }
  //
  //  @DeleteMapping("/user/{userId}/remove")
  //  public ResponseEntity<String> delete(@PathVariable long userId) throws UserNotFoundException {
  //    userService.deleteById(userId);
  //    return ResponseEntity.ok("User deleted successfully");
  //  }
}
