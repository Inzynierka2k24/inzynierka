import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class FinanceSourceService {

  constructor(private httpClient: HttpClient) {}

  getEventsWithSources() {
    return this.httpClient.get<Map<string, string[]>>('/api/finance-source');
  }

}
