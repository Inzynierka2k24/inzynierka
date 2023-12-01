import { Injectable } from '@angular/core';
import { ExternalAccount } from '../../../generated';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class ExternalAccountsService {
  constructor(private httpClient: HttpClient) {}

  get(id: number) {
    return this.httpClient.get<ExternalAccount[]>(
      `${environment.api_url}/${id}/external/account`,
    );
  }

  add(id: number, newAccount: ExternalAccount) {
    return this.httpClient.post(
      `${environment.api_url}/${id}/external/account`,
      newAccount,
    );
  }
}
