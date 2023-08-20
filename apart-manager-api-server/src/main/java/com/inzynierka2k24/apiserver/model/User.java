package com.inzynierka2k24.apiserver.model;

public record User(int id, String userName, String password, boolean active, String roles) {}
