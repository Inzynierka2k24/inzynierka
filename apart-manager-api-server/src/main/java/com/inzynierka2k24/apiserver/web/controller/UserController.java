package com.inzynierka2k24.apiserver.web.controller;

import com.inzynierka2k24.apiserver.service.UserService;
import com.inzynierka2k24.apiserver.web.request.AuthRequest;
import com.inzynierka2k24.apiserver.web.request.EditUserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {
  private final UserService userService;

  //  @PostMapping("/login")
  //  public boolean login(@RequestBody AuthRequest request) {
  //    return userService.get(request.mail(), request.password()).isPresent();
  //  }

  @PostMapping("/register")
  public void register(@RequestBody AuthRequest request) {
    userService.register(request.mail(), request.password());
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
