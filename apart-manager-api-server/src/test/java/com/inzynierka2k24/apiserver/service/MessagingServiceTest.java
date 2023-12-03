package com.inzynierka2k24.apiserver.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.inzynierka2k24.apiserver.dao.ApartmentDao;
import com.inzynierka2k24.apiserver.dao.ContactDao;
import com.inzynierka2k24.apiserver.dao.ScheduledMessageDao;
import com.inzynierka2k24.apiserver.model.*;
import com.inzynierka2k24.apiserver.web.dto.ContactDTO;
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

  //  @Test
  //  public void shouldMapContactToDTO() {
  //    // given
  //    long userId = 1;
  //    Contact contact =
  //        new Contact(
  //            Optional.of(1L),
  //            Optional.of(userId),
  //            ContactType.CLEANING,
  //            "John",
  //            "mail",
  //            "phone",
  //            false,
  //            false,
  //            100);
  //    ScheduledMessageDTO messageDTO =
  //        new ScheduledMessageDTO(
  //            Optional.of(1L),
  //            new ArrayList<>(),
  //            "message",
  //            IntervalType.DAYS,
  //            1,
  //            TriggerType.CHECKIN);
  //    Apartment apartment =
  //        new Apartment(
  //            Optional.of(1L), 100.0f, "Apartment 1", "Country 1", "City 1", "Street 1", "1",
  // "1A");
  //    messageDTO.apartments().add(apartment);
  //    when(messageDao.getAllForUser(userId, contact.id().get())).thenReturn(new ArrayList<>());
  //    when(messageDao.getApartmentsForMessage(messageDTO.id().get()))
  //        .thenReturn(List.of(apartment.id().get()));
  //    when(apartmentDao.getById(userId, apartment.id().get())).thenReturn(Optional.of(apartment));
  //    // when
  //    ContactDTO actualContact = messagingService.mapContactToDTO(userId, contact);
  //    // then
  //    assertEquals(contact.id(), actualContact.id());
  //    assertEquals(contact.contactType(), actualContact.contactType());
  //    assertEquals(contact.name(), actualContact.name());
  //    assertEquals(contact.price(), actualContact.price());
  //    assertEquals(contact.mail(), actualContact.mail());
  //    assertEquals(contact.phone(), actualContact.phone());
  //    assertEquals(new NotificationSettings(false, false), actualContact.notificationSettings());
  //    assertEquals(List.of(messageDTO), actualContact.messages());
  //    assertEquals(List.of(apartment), actualContact.apartments());
  //  }

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
    when(messageDao.getAllForUser(contact.userId().get(), contact.id().get()))
        .thenReturn(expectedMessages);
    // when
    List<ScheduledMessage> actualMessages = messagingService.getMessagesForContact(contact);
    // then
    assertEquals(expectedMessages, actualMessages);
  }
}
