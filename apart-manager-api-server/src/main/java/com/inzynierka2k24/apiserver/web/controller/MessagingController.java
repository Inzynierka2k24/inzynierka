package com.inzynierka2k24.apiserver.web.controller;

import com.inzynierka2k24.apiserver.model.ContactType;
import com.inzynierka2k24.apiserver.service.MessagingService;
import com.inzynierka2k24.apiserver.web.dto.ContactDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class MessagingController {

    private final MessagingService messagingService;

    @GetMapping("/messaging/{userId}")
    public ResponseEntity<List<ContactDTO>> getContactsForUser(@PathVariable String userId){
        return ResponseEntity.ok(Collections.singletonList(new ContactDTO(ContactType.UNKNOWN, "Bonk")));
    }

}
