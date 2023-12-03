package com.inzynierka2k24.apiserver.service;

import com.inzynierka2k24.apiserver.dao.ApartmentDao;
import com.inzynierka2k24.apiserver.dao.ContactDao;
import com.inzynierka2k24.apiserver.dao.ScheduledMessageDao;
import com.inzynierka2k24.apiserver.model.Apartment;
import com.inzynierka2k24.apiserver.model.Contact;
import com.inzynierka2k24.apiserver.model.ScheduledMessage;
import com.inzynierka2k24.apiserver.web.dto.ContactDTO;
import com.inzynierka2k24.apiserver.web.dto.ScheduledMessageDTO;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessagingService {

  private final ContactDao contactDao;
  private final ScheduledMessageDao messageDao;
  private final ApartmentDao apartmentDao;

  public List<ContactDTO> getContacts(long userId) {
    List<Contact> contacts = contactDao.getAll(userId);

    if (contacts.isEmpty()) {
      return new ArrayList<>();
    }
    // map to ContactDTO
    return contacts.stream().map(contact -> mapContactToDTO(userId, contact)).toList();
  }

  public ContactDTO mapContactToDTO(long userId, Contact contact) {
    ContactDTO contactDTO =
        new ContactDTO(
            contact.id().orElse(0L),
            contact.contactType(),
            contact.name(),
            contact.price(),
            contact.mail(),
            contact.phone());

    List<ScheduledMessage> messages = getMessagesForContact(contact);

    messages.forEach(
        message -> {
          List<Long> apartmentIds = messageDao.getApartmentsForMessage(message.id().get());
          List<Apartment> apartments =
              apartmentIds.stream()
                  .map(apartment -> apartmentDao.getById(userId, apartment).get())
                  .toList();
          contactDTO
              .messages()
              .add(
                  new ScheduledMessageDTO(
                      message.id(),
                      apartments,
                      message.message(),
                      message.intervalType(),
                      message.intervalValue(),
                      message.triggerType()));
        });
    return contactDTO;
  }

  public List<ScheduledMessage> getMessagesForContact(Contact contact) {
    if (contact.userId().isEmpty() || contact.id().isEmpty()) {
      throw new RuntimeException("Contact has no id or user id");
    }
    return messageDao.getAllForUser(contact.userId().get(), contact.id().get());
  }

  public void addContact(long userId, Contact contact) {
    contactDao.add(userId, contact);
  }

  public void addMessage(long userId, long contactId, ScheduledMessageDTO message) {
    message
        .apartments()
        .forEach(
            apartment -> {
              if (apartment.id().isPresent()) {
                ScheduledMessage scheduledMessage =
                    new ScheduledMessage(
                        userId,
                        contactId,
                        message.message(),
                        message.intervalType(),
                        message.intervalValue(),
                        message.triggerType());
                messageDao.add(userId, apartment.id().get(), scheduledMessage);
              }
            });
  }

  public Contact getContactById(long userId, long contactId) {
    return contactDao.getById(userId, contactId).orElseThrow();
  }

  public void deleteMessage(long userId, long contactId, long messageId) {
    messageDao.deleteById(userId, contactId, messageId);
  }

  public void deleteContact(long userId, long contactId) {
    contactDao.deleteById(userId, contactId);
  }

  public void updateContact(long userId, Contact contact) {
    contactDao.update(userId, contact);
  }
}
