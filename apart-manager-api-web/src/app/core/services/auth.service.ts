import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { AuthRequest, RegisterRequest } from '../../../generated';
import { Observable } from 'rxjs';

const PROXY_PREFIX = '/api';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  constructor(private httpClient: HttpClient) {}

  login(request: AuthRequest): Observable<string> {
    return this.httpClient.post(PROXY_PREFIX + '/login', request, {
      responseType: 'text',
    });
  }

  register(request: RegisterRequest): Observable<string> {
    return this.httpClient.post<string>(PROXY_PREFIX + '/register', request);
  }
}
