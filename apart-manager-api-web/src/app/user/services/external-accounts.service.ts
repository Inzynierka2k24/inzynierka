import { Injectable } from '@angular/core';
import { ExternalAccount } from '../../../generated';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class ExternalAccountsService {
  constructor(private httpClient: HttpClient) {}

  get(userId: number) {
    return this.httpClient.get<ExternalAccount[]>(
      `${environment.api_url}/${userId}/external/account`,
    );
  }

  add(userId: number, newAccount: ExternalAccount) {
    return this.httpClient.post<string>(
      `${environment.api_url}/${userId}/external/account`,
      newAccount,
    );
  }

  edit(userId: number, account: ExternalAccount) {
    return this.httpClient.put<string>(
      `${environment.api_url}/${userId}/external/account`,
      account,
      { responseType: 'text' as 'json' },
    );
  }

  delete(userId: number, accountId: number) {
    return this.httpClient.delete<string>(
      `${environment.api_url}/${userId}/external/account/${accountId}`,
    );
  }
}
