package com.inzynierka2k24.apiserver.service;

import com.inzynierka2k24.apiserver.dao.ApartmentDao;
import com.inzynierka2k24.apiserver.dao.ContactDao;
import com.inzynierka2k24.apiserver.dao.ScheduledMessageDao;
import com.inzynierka2k24.apiserver.exception.apartment.ApartmentNotFoundException;
import com.inzynierka2k24.apiserver.exception.messaging.ContactNotFoundException;
import com.inzynierka2k24.apiserver.exception.user.UserNotFoundException;
import com.inzynierka2k24.apiserver.model.Apartment;
import com.inzynierka2k24.apiserver.model.Contact;
import com.inzynierka2k24.apiserver.model.ScheduledMessage;
import com.inzynierka2k24.apiserver.web.dto.ContactDTO;
import com.inzynierka2k24.apiserver.web.dto.ScheduledMessageDTO;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessagingService {

  private final ContactDao contactDao;
  private final ScheduledMessageDao messageDao;
  private final ApartmentDao apartmentDao;

  public List<ContactDTO> getContacts(long userId) throws UserNotFoundException {
    List<Contact> contacts = contactDao.getAll(userId);

    if (contacts.isEmpty()) {
      throw new UserNotFoundException();
    }
    // map to ContactDTO
    return contacts.stream()
        .map(
            contact -> {
              try {
                return mapContactToDTO(userId, contact);
              } catch (ContactNotFoundException e) {
                throw new RuntimeException(e);
              }
            })
        .collect(Collectors.toList());
  }

  public ContactDTO mapContactToDTO(long userId, Contact contact) throws ContactNotFoundException {
    if (contact.id().isEmpty()) {
      throw new ContactNotFoundException();
    }
    ContactDTO contactDTO =
        new ContactDTO(
            contact.id().get(),
            contact.contactType(),
            contact.name(),
            contact.price(),
            contact.mail(),
            contact.phone());

    List<ScheduledMessage> messages = getMessagesForContact(contact);

    messages.forEach(
        message -> {
          if (message.id().isEmpty()) {
            throw new RuntimeException("Message has no id");
          }
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

  public void addMessage(long userId, long contactId, ScheduledMessageDTO message)
      throws ApartmentNotFoundException, ContactNotFoundException {
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
}