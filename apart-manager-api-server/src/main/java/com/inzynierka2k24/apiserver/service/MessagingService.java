package com.inzynierka2k24.apiserver.service;

import com.inzynierka2k24.apiserver.dao.ContactDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessagingService {

    private final ContactDao contactDao;

}
