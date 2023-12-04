import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class FinanceSourceService {

  constructor(private httpClient: HttpClient) {}

  getEventsWithSources() {
    return this.httpClient.get<Map<string, string[]>>(`${environment.api_url}` + '/finance-source');
  }
}
