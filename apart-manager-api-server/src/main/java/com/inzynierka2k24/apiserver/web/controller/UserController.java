package com.inzynierka2k24.apiserver.web.controller;

import com.inzynierka2k24.apiserver.service.UserService;
import com.inzynierka2k24.apiserver.web.request.EditUserRequest;
import com.inzynierka2k24.apiserver.web.request.LoginRequest;
import com.inzynierka2k24.apiserver.web.request.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {
  private final UserService userService;

  @PostMapping("/login")
  public boolean login(@RequestBody LoginRequest request) {
    return userService.get(request.login(), request.password()).isPresent();
  }

  @PostMapping("/register")
  public void register(@RequestBody RegisterRequest request) {
    userService.register(request.login(), request.password(), request.mail());
  }

  @PutMapping("/user/{userId}/edit")
  public void edit(@PathVariable long userId, @RequestBody EditUserRequest request) {
    userService.update(userId, request.password(), request.mail());
  }

  @DeleteMapping("/user/{userId}/remove")
  public void delete(@PathVariable long userId) {
    userService.deleteById(userId);
  }
}
