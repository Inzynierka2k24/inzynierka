import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { EditUserRequest, UserDTO } from '../../../generated';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  constructor(private httpClient: HttpClient) {}

  getUserDetails() {
    return this.httpClient.get<UserDTO>('/api/user/details');
  }

  editUser(user: number, editUserRequest: EditUserRequest) {
    return this.httpClient.put<EditUserRequest>(
      '/api/user/' + user + '/edit',
      editUserRequest,
    );
  }
}
