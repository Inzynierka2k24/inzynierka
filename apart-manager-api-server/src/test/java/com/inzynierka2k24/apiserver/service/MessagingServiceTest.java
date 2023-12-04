package com.inzynierka2k24.apiserver.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.inzynierka2k24.apiserver.dao.ApartmentDao;
import com.inzynierka2k24.apiserver.dao.ContactDao;
import com.inzynierka2k24.apiserver.dao.ScheduledMessageDao;
import com.inzynierka2k24.apiserver.model.*;
import com.inzynierka2k24.apiserver.web.dto.ContactDTO;
import com.inzynierka2k24.apiserver.web.dto.ScheduledMessageDTO;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

public class MessagingServiceTest {
  private final ContactDao contactDao = mock(ContactDao.class);
  private final ScheduledMessageDao messageDao = mock(ScheduledMessageDao.class);
  private final ApartmentDao apartmentDao = mock(ApartmentDao.class);

  private final MessagingService messagingService =
      new MessagingService(contactDao, messageDao, apartmentDao);

  @Test
  public void shouldGetAllContactsForUserId() {
    // given
    long userId = 1;
    List<Contact> contacts = new ArrayList<>();
    contacts.add(
        new Contact(
            Optional.of(1L),
            Optional.of(userId),
            ContactType.CLEANING,
            "John",
            "mail",
            "phone",
            false,
            false,
            100));
    List<ContactDTO> expectedContacts =
        contacts.stream()
            .map(contact -> messagingService.mapContactToDTO(userId, contact))
            .toList();
    when(contactDao.getAll(userId)).thenReturn(contacts);
    // when
    List<ContactDTO> actualContacts = messagingService.getContacts(userId);
    // then
    assertEquals(expectedContacts, actualContacts);
  }

  @Test
  public void shouldMapContactToDTO() {
    // given
    long userId = 1;
    Contact contact =
        new Contact(
            Optional.of(1L),
            Optional.of(userId),
            ContactType.CLEANING,
            "John",
            "mail",
            "phone",
            false,
            false,
            100);
    //      ScheduledMessageDTO messageDTO =
    //          new ScheduledMessageDTO(
    //              Optional.of(1L),
    //              new ArrayList<>(),
    //              "message",
    //              IntervalType.DAYS,
    //              1,
    //              TriggerType.CHECKIN);
    ScheduledMessage message =
        new ScheduledMessage(1L, 1L, "message", IntervalType.DAYS, 1, TriggerType.CHECKIN);
    Apartment apartment =
        new Apartment(
            Optional.of(1L), 100.0f, "Apartment 1", "Country 1", "City 1", "Street 1", "1", "1A");
    // messageDTO.apartments().add(apartment);
    when(messageDao.getAllForUser(userId, contact.id().orElseThrow()))
        .thenReturn(new ArrayList<>());
    when(messageDao.getApartmentsForMessage(message.id().orElseThrow()))
        .thenReturn(List.of(apartment.id().orElseThrow()));
    when(apartmentDao.getById(userId, apartment.id().orElseThrow()))
        .thenReturn(Optional.of(apartment));
    when(messagingService.getMessagesForContact(contact)).thenReturn(List.of(message));
    // when
    ContactDTO actualContact = messagingService.mapContactToDTO(userId, contact);
    // then
    assertEquals(contact.id(), actualContact.id());
    assertEquals(contact.contactType(), actualContact.contactType());
    assertEquals(contact.name(), actualContact.name());
    assertEquals(contact.price(), actualContact.price());
    assertEquals(contact.mail(), actualContact.mail());
    assertEquals(contact.phone(), actualContact.phone());
    assertEquals(new NotificationSettings(false, false), actualContact.notificationSettings());
    assertEquals(actualContact.messages().size(), 1);
  }

  @Test
  public void shouldGetMessagesForContact() {
    // given
    Contact contact =
        new Contact(
            Optional.of(1L),
            Optional.of(1L),
            ContactType.CLEANING,
            "John",
            "mail",
            "phone",
            false,
            false,
            100);
    ScheduledMessage message =
        new ScheduledMessage(1L, 1L, "message", IntervalType.DAYS, 1, TriggerType.CHECKIN);
    List<ScheduledMessage> expectedMessages = new ArrayList<>();
    expectedMessages.add(message);
    when(messageDao.getAllForUser(contact.userId().orElseThrow(), contact.id().orElseThrow()))
        .thenReturn(expectedMessages);
    // when
    List<ScheduledMessage> actualMessages = messagingService.getMessagesForContact(contact);
    // then
    assertEquals(expectedMessages, actualMessages);
  }

  @Test
  void shouldUpdateContact() {
    // given
    long userId = 1L;
    Contact contact =
        new Contact(
            Optional.of(1L),
            Optional.of(1L),
            ContactType.CLEANING,
            "John",
            "mail",
            "phone",
            false,
            false,
            100);
    doNothing().when(contactDao).update(userId, contact);
    // when
    messagingService.updateContact(userId, contact);
    // then
    verify(contactDao, times(1)).update(userId, contact);
  }

  @Test
  void shouldAddContact() {
    // given
    long userId = 1L;
    Contact contact =
        new Contact(
            Optional.of(1L),
            Optional.of(1L),
            ContactType.CLEANING,
            "John",
            "mail",
            "phone",
            false,
            false,
            100);
    doNothing().when(contactDao).add(userId, contact);
    // when
    messagingService.addContact(userId, contact);
    // then
    verify(contactDao, times(1)).add(userId, contact);
  }

  @Test
  void shouldDeleteContact() {
    // given
    long userId = 1L;
    long contactId = 1L;
    doNothing().when(contactDao).deleteById(userId, contactId);
    // when
    messagingService.deleteContact(userId, contactId);
    // then
    verify(contactDao, times(1)).deleteById(userId, contactId);
  }

  @Test
  void shouldDeleteMessage() {
    // given
    long userId = 1L;
    long contactId = 1L;
    long messageId = 1L;
    doNothing().when(messageDao).deleteById(userId, contactId, messageId);
    // when
    messagingService.deleteMessage(userId, contactId, messageId);
    // then
    verify(messageDao, times(1)).deleteById(userId, contactId, messageId);
  }

  @Test
  void shouldAddMessage() {
    // given
    long userId = 1L;
    long contactId = 1L;
    ScheduledMessageDTO messageDTO =
        new ScheduledMessageDTO(
            Optional.of(1L),
            new ArrayList<>(),
            "message",
            IntervalType.DAYS,
            1,
            TriggerType.CHECKIN);
    ScheduledMessage message =
        new ScheduledMessage(1L, 1L, "message", IntervalType.DAYS, 1, TriggerType.CHECKIN);
    Apartment apartment =
        new Apartment(
            Optional.of(1L), 100.0f, "Apartment 1", "Country 1", "City 1", "Street 1", "1", "1A");
    messageDTO.apartments().add(apartment);
    doNothing().when(messageDao).add(userId, apartment.id().orElseThrow(), message);
    when(apartmentDao.getById(userId, apartment.id().orElseThrow()))
        .thenReturn(Optional.of(apartment));
    // when
    messagingService.addMessage(userId, contactId, messageDTO);
    // then
    verify(messageDao, times(1)).add(userId, apartment.id().orElseThrow(), message);
  }

  @Test
  void shouldGetContactById() {
    // given
    long userId = 1L;
    long contactId = 1L;
    Contact contact =
        new Contact(
            Optional.of(1L),
            Optional.of(1L),
            ContactType.CLEANING,
            "John",
            "mail",
            "phone",
            false,
            false,
            100);
    when(contactDao.getById(userId, contactId)).thenReturn(Optional.of(contact));
    // when
    Contact actualContact = messagingService.getContactById(userId, contactId);
    // then
    assertEquals(contact, actualContact);
  }
}
