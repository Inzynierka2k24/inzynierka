import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { EditUserRequest, UserDTO } from '../../../generated';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  constructor(private httpClient: HttpClient) {}

  getUserDetails() {
    return this.httpClient.get<UserDTO>(`${environment.api_url}/user/details`);
  }

  editUser(user: number, editUserRequest: EditUserRequest) {
    return this.httpClient.put(
      `${environment.api_url}/user/` + user + '/edit',
      editUserRequest,
      { responseType: 'text' },
    );
  }
}
