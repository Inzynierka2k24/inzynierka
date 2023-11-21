import { Injectable } from '@angular/core';
import {HttpClient, HttpEvent} from '@angular/common/http';
import { Observable } from 'rxjs';
import {Finance, UserDTO} from "../../../generated";

@Injectable({
  providedIn: 'root'
})
export class FinanceService {

  constructor(private http: HttpClient) { }

  getFinances(user: UserDTO): Observable<Finance[]> {
    return this.http.get<Finance[]>('/api/'+ user.id +'/finance');
  }

  addFinance(user: UserDTO, finance: Finance, options?: any): Observable<HttpEvent<boolean>> {
    return this.http.post<boolean>('/api/'+ user.id +'/finance', finance, options);
  }

  updateFinance(user: UserDTO, finance: Finance): Observable<Finance> {
    return this.http.put<Finance>('/api/'+ user.id +'/finance/' + finance.id, finance);
  }

  deleteFinance(user: UserDTO, financeId: number, options?: any): Observable<HttpEvent<void>>{
    return this.http.delete<void>('/api/'+ user.id +'/finance/' + financeId, options);
  }
}
