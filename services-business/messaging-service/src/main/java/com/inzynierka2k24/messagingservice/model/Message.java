package com.inzynierka2k24.messagingservice.model;

import com.inzynierka2k24.MessageType;
import lombok.With;

public record Message(MessageContent content, @With MessageDetails details, MessageType type) {}
