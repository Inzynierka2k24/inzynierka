package com.inzynierka2k24.apiserver.model;

import java.util.Optional;

public record UserPreferences(Optional<Long> id, boolean sms, boolean email) {}
