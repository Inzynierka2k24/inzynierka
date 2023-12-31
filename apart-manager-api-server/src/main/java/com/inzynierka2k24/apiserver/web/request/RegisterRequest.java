package com.inzynierka2k24.apiserver.web.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
    String login, @Email String emailAddress, @Size(min = 5) String password) {}
