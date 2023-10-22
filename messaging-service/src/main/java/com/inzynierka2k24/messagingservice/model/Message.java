package com.inzynierka2k24.messagingservice.model;

import com.inzynierka2k24.MessageType;

public record Message(MessageContent content, MessageDetails details, MessageType type) {}
