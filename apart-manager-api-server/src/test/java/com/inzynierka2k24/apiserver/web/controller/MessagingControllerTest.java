package com.inzynierka2k24.apiserver.web.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.inzynierka2k24.apiserver.grpc.messaging.MessagingServiceClient;
import com.inzynierka2k24.apiserver.model.*;
import com.inzynierka2k24.apiserver.service.MessagingService;
import com.inzynierka2k24.apiserver.web.dto.ScheduledMessageDTO;
import java.util.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(MessagingController.class)
@AutoConfigureJsonTesters
class MessagingControllerTest {
  @Autowired JacksonTester<ScheduledMessageDTO> scheduledMessageJacksonTester;
  @MockBean MessagingService messagingService;
  @MockBean MessagingServiceClient messagingServiceClient;

  @Autowired private MockMvc mockMvc;

  @Test
  @WithMockUser
  void shouldGetAddMessageRequestReturnsString() throws Exception {
    // given
    long userId = 1;
    long contactId = 1;
    ScheduledMessageDTO scheduledMessageDTO =
        new ScheduledMessageDTO(
            Optional.of(userId),
            new ArrayList<>(),
            "test",
            IntervalType.DAYS,
            4,
            TriggerType.CHECKIN);
    Contact contact =
        new Contact(
            Optional.of(contactId),
            Optional.of(userId),
            ContactType.CLEANING,
            "test",
            "test",
            "test",
            false,
            false,
            24);
    doNothing().when(messagingService).addMessage(userId, contactId, scheduledMessageDTO);
    when(messagingService.getContactById(userId, contactId)).thenReturn(contact);
    doNothing().when(messagingServiceClient).sendMessage(contact, scheduledMessageDTO);
    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.post(
                    String.format("/messaging/%s/contact/%s", userId, contactId))
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(scheduledMessageJacksonTester.write(scheduledMessageDTO).getJson()))
        .andExpect(status().isCreated())
        .andExpect(content().string("Message successfully added"));
    verify(messagingService, times(1)).addMessage(userId, contactId, scheduledMessageDTO);
    verify(messagingService, times(1)).getContactById(userId, contactId);
    verify(messagingServiceClient, times(1)).sendMessage(contact, scheduledMessageDTO);
  }

  @Test
  @WithMockUser
  void shouldGetDeleteMessageRequestReturnsString() throws Exception {
    // given
    long userId = 1;
    long contactId = 1;
    long messageId = 1;
    doNothing().when(messagingService).deleteMessage(userId, contactId, messageId);
    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.delete(
                    String.format(
                        "/messaging/%s/contact/%s/message/%s", userId, contactId, messageId))
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().string("Message successfully deleted"));
    verify(messagingService, times(1)).deleteMessage(userId, contactId, messageId);
  }
}
