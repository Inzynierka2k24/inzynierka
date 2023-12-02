import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Contact, ContactDTO, ScheduledMessageDTO } from '../../generated';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class MessagingService {
  constructor(private httpClient: HttpClient) {}

  getContactsForUser(userId: number) {
    return this.httpClient.get<ContactDTO[]>('/api/contacts/' + userId);
  }

  addOrder(userId: number, contactId: number, message: ScheduledMessageDTO) {
    return this.httpClient.post<ScheduledMessageDTO>(
      '/api/messaging/' + userId + '/contact/' + contactId,
      message,
    );
  }

  addContact(userId: number, contact: Contact) {
    return this.httpClient.post(
      `${environment.api_url}/contacts/` + userId,
      contact,
      { responseType: 'text' },
    );
  }

  deleteMessage(userId: number, contactId: number, id: number) {
    return this.httpClient.delete(
      `${environment.api_url}/messaging/` +
        userId +
        '/contact/' +
        contactId +
        '/message/' +
        id,
      { responseType: 'text' },
    );
  }

  deleteContact(userId: number, contactId: number) {
    return this.httpClient.delete(
      `${environment.api_url}/contacts/` + userId + '/' + contactId,
      { responseType: 'text' },
    );
  }
}
