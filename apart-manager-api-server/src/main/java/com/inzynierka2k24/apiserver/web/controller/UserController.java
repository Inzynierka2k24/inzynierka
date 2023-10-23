package com.inzynierka2k24.apiserver.web.controller;

import com.inzynierka2k24.apiserver.exception.user.UserAlreadyExistsException;
import com.inzynierka2k24.apiserver.exception.user.UserNotFoundException;
import com.inzynierka2k24.apiserver.model.User;
import com.inzynierka2k24.apiserver.service.UserService;
import com.inzynierka2k24.apiserver.web.request.AuthRequest;
import com.inzynierka2k24.apiserver.web.request.EditUserRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;
  private final PasswordEncoder passwordEncoder;

  @PostMapping("/login")
  public ResponseEntity<String> login(@Valid @RequestBody AuthRequest request) {
    UserDetails userDetails = userService.loadUserByUsername(request.mail());
    if (passwordEncoder.matches(request.password(), userDetails.getPassword())) {
      return ResponseEntity.ok("Successfuly logged in");
    }
    return ResponseEntity.status(401).body("Invalid credentials");
  }

  @PostMapping("/register")
  public ResponseEntity<String> register(@Valid @RequestBody AuthRequest request)
      throws UserAlreadyExistsException {
    userService.register(new User(request.mail(), passwordEncoder.encode(request.password())));
    return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
  }

  @PutMapping("/user/{userId}/edit")
  public ResponseEntity<String> edit(
      @PathVariable long userId, @Valid @RequestBody EditUserRequest request)
      throws UserNotFoundException {
    userService.update(
        new User(userId, request.mail(), passwordEncoder.encode(request.password())));
    return ResponseEntity.ok("User updated successfully");
  }

  @DeleteMapping("/user/{userId}/remove")
  public ResponseEntity<String> delete(@PathVariable long userId) throws UserNotFoundException {
    userService.deleteById(userId);
    return ResponseEntity.ok("User deleted successfully");
  }
}
