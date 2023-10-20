import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { AuthRequest, User } from '../../../generated';
import { Observable, of } from 'rxjs';

const PROXY_PREFIX = '/api';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  constructor(private httpClient: HttpClient) {}

  login(request: AuthRequest): Observable<User> {
    return this.httpClient.post<User>(PROXY_PREFIX + '/login', request);
  }

  register(request: AuthRequest): Observable<boolean> {
    return this.httpClient.post<boolean>(PROXY_PREFIX + '/register', request);
  }

  authenticate(): Observable<boolean> {
    return of(false); //TODO
  }

  logout() {}
}
