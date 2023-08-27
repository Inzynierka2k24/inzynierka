package com.inzynierka2k24.apiserver.model;

public record ExternalAccount(
    long id, String login, String password, String mail, ServiceType serviceType) {}
