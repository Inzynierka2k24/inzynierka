package com.inzynierka2k24.apiserver.web.dto;

import com.inzynierka2k24.apiserver.model.Apartment;
import com.inzynierka2k24.apiserver.model.ContactType;

import java.util.ArrayList;
import java.util.List;

public record ContactDTO(
    ContactType contactType,
    String name,
    List<String> messages,
    List<Apartment> apartments){
    public ContactDTO(ContactType contactType, String name) {
        this(contactType,name, new ArrayList<>(), new ArrayList<>());
    }
}

