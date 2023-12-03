package com.inzynierka2k24.apiserver.web.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.inzynierka2k24.apiserver.model.Contact;
import com.inzynierka2k24.apiserver.model.ContactType;
import com.inzynierka2k24.apiserver.model.NotificationSettings;
import com.inzynierka2k24.apiserver.service.MessagingService;
import com.inzynierka2k24.apiserver.web.dto.ContactDTO;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

@WebMvcTest(ContactController.class)
@AutoConfigureJsonTesters
class ContactControllerTest {
  @Autowired JacksonTester<List<ContactDTO>> contactDTOListJacksonTester;
  @Autowired JacksonTester<Contact> contactJacksonTester;
  @MockBean MessagingService messagingService;

  @Autowired private MockMvc mockMvc;

  @Test
  @WithMockUser
  void shouldGetContactsForUserReturnsListOfContacts() throws Exception {
    // given
    long userId = 1;
    long contactId = 1;
    ContactDTO contactDTO =
        new ContactDTO(
            Optional.of(contactId),
            ContactType.CLEANING,
            "test",
            4,
            "test",
            "test",
            new NotificationSettings(false, false),
            new ArrayList<>());
    when(messagingService.getContacts(userId)).thenReturn(List.of(contactDTO));

    // when/then
    var response =
        mockMvc
            .perform(MockMvcRequestBuilders.get("/contacts/" + userId))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andReturn();

    assertEquals(
        contactDTOListJacksonTester.write(List.of(contactDTO)).getJson(),
        response.getResponse().getContentAsString());
    verify(messagingService).getContacts(userId);
  }

  @Test
  @WithMockUser
  void shouldGetAddContactRequestReturnsString() throws Exception {
    // given
    long userId = 1;
    Contact contact =
        new Contact(
            Optional.empty(),
            Optional.of(userId),
            ContactType.CLEANING,
            "test",
            "test",
            "test",
            false,
            false,
            3);
    doNothing().when(messagingService).addContact(userId, contact);
    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/contacts/" + userId)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(contactJacksonTester.write(contact).getJson()))
        // then
        .andExpect(status().isCreated())
        .andExpect(content().string("Successfully added contact"));
    verify(messagingService, times(1)).addContact(userId, contact);
  }

  @Test
  @WithMockUser
  void shouldGetUpdateContactRequestReturnsString() throws Exception {
    // given
    long userId = 1;
    long contactId = 1;
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
            3);
    doNothing().when(messagingService).updateContact(userId, contact);
    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.put("/contacts/" + userId + "/edit")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(contactJacksonTester.write(contact).getJson()))
        // then
        .andExpect(status().isOk())
        .andExpect(content().string("Successfully updated contact"));
    verify(messagingService, times(1)).updateContact(userId, contact);
  }

  @Test
  @WithMockUser
  void shouldGetDeleteContactRequestReturnsString() throws Exception {
    // given
    long userId = 1;
    long contactId = 1;
    doNothing().when(messagingService).deleteContact(userId, contactId);
    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.delete("/contacts/" + userId + "/" + contactId)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON))
        // then
        .andExpect(status().isOk())
        .andExpect(content().string("Successfully deleted contact"));
    verify(messagingService, times(1)).deleteContact(userId, contactId);
  }
}
