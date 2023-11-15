import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {Finance} from "../../../generated";

@Injectable({
  providedIn: 'root'
})
export class FinanceService {

  constructor(private http: HttpClient) { }

  getFinances(): Observable<Finance[]> {
    return this.http.get<Finance[]>('/api/11/finance');
    //TODO change path
  }

  addFinance(finance: Finance): Observable<boolean> {
    return this.http.post<boolean>('/api/11/finance', finance);
  }

  updateFinance(finance: Finance): Observable<Finance> {
    return this.http.put<Finance>('/api/11/finance/' + finance.id, finance);
  }

  deleteFinance(financeId: number): Observable<void> {
    return this.http.delete<void>('/api/11/finance/' + financeId);
  }
}
