package com.inzynierka2k24.apiserver.model;

public record Contact(long id, ContactType contactType, String receiver, String message) {}
