package com.inzynierka2k24.apiserver.web.controller;

import com.inzynierka2k24.apiserver.model.User;
import com.inzynierka2k24.apiserver.service.UserService;
import com.inzynierka2k24.apiserver.web.request.AuthRequest;
import com.inzynierka2k24.apiserver.web.request.EditUserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;
  private final PasswordEncoder passwordEncoder;

  @PostMapping("/register")
  public ResponseEntity<String> register(@RequestBody AuthRequest request) {
    if (userService.existsByMail(request.mail())) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("User with the same email already exists");
    }

    userService.register(new User(request.mail(), passwordEncoder.encode(request.password())));
    return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
  }

  @PutMapping("/user/{userId}/edit")
  public ResponseEntity<String> edit(
      @PathVariable long userId, @RequestBody EditUserRequest request) {
    userService.update(
        new User(userId, request.mail(), passwordEncoder.encode(request.password())));
    return ResponseEntity.ok("User updated successfully");
  }

  @DeleteMapping("/user/{userId}/remove")
  public ResponseEntity<String> delete(@PathVariable long userId) {
    userService.deleteById(userId);
    return ResponseEntity.noContent().build();
  }
}
