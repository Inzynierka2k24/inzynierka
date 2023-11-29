import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { AuthRequest, RegisterRequest } from '../../../generated';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  constructor(private httpClient: HttpClient) {}

  login(request: AuthRequest): Observable<string> {
    return this.httpClient.post(`${environment.api_url}` + '/login', request, {
      responseType: 'text',
    });
  }

  register(request: RegisterRequest): Observable<string> {
    return this.httpClient.post<string>(`${environment.api_url}` + '/register', request);
  }
}
