import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class MessagingService {
  constructor(private httpClient: HttpClient) {}

  getContactsForUser(userId: number) {}
}
