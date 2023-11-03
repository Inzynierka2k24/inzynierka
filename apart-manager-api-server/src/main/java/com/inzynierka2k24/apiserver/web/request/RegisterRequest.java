package com.inzynierka2k24.apiserver.web.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;

public record RegisterRequest(String login, @Valid @Email String emailAddress, String password) {
}
