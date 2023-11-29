package com.inzynierka2k24.apiserver.service;

import com.inzynierka2k24.apiserver.dao.ContactDao;
import com.inzynierka2k24.apiserver.dao.ScheduledMessageDao;
import com.inzynierka2k24.apiserver.exception.apartment.ApartmentNotFoundException;
import com.inzynierka2k24.apiserver.exception.messaging.ContactNotFoundException;
import com.inzynierka2k24.apiserver.exception.user.UserNotFoundException;
import com.inzynierka2k24.apiserver.model.Contact;
import com.inzynierka2k24.apiserver.model.ScheduledMessage;
import com.inzynierka2k24.apiserver.web.dto.ContactDTO;
import com.inzynierka2k24.apiserver.web.dto.ScheduledMessageDTO;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessagingService {

  private final ContactDao contactDao;
  private final ScheduledMessageDao messageDao;

  public List<ContactDTO> getContacts(long userId) throws UserNotFoundException {
    List<Contact> contacts = contactDao.getAll(userId);

    if (contacts.isEmpty()) {
      throw new UserNotFoundException();
    }
    // map to ContactDTO
    return contacts.stream()
        .map(
            contact ->
                new ContactDTO(
                    contact.id().get(),
                    contact.contactType(),
                    contact.name(),
                    contact.price(),
                    contact.mail(),
                    contact.phone()))
        .collect(Collectors.toList());
  }

  public void addContact(long userId, Contact contact) {
    contactDao.add(userId, contact);
  }

  public void addMessage(long userId, ScheduledMessageDTO message)
      throws ApartmentNotFoundException, ContactNotFoundException {
    long contactId = message.contact().id().orElseThrow(ContactNotFoundException::new);

    message
        .apartments()
        .forEach(
            apartment -> {
              Optional<Long> apartmentId = apartment.id();

              ScheduledMessage scheduledMessage =
                  new ScheduledMessage(
                      userId,
                      contactId,
                      apartmentId.get(),
                      message.message(),
                      message.intervalType(),
                      message.intervalValue(),
                      message.triggerType());

              messageDao.add(userId, scheduledMessage);
            });
  }
}
