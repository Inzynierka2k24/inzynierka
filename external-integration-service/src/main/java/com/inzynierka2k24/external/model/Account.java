package com.inzynierka2k24.external.model;

import com.inzynierka2k24.ExternalService;

public record Account(String login, String password, ExternalService service) {}
