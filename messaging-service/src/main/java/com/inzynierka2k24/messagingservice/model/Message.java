package com.inzynierka2k24.messagingservice.model;

import lombok.With;

public record Message(@With String receiver, String content) {}
