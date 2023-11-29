import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Contact, ContactDTO, ScheduledMessageDTO } from '../../generated';

@Injectable({
  providedIn: 'root',
})
export class MessagingService {
  constructor(private httpClient: HttpClient) {}

  getContactsForUser(userId: number) {
    return this.httpClient.get<ContactDTO[]>('/api/contacts/' + userId);
  }

  addOrder(userId: number, message: ScheduledMessageDTO) {
    return this.httpClient.post<ScheduledMessageDTO>(
      '/api/messaging/' + userId,
      message,
    );
  }

  addContact(userId: number, contact: Contact) {
    return this.httpClient.post<Contact>('/api/contacts/' + userId, contact);
  }
}
