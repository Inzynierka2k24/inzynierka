import { Injectable } from '@angular/core';
import {HttpClient, HttpEvent} from '@angular/common/http';
import { Observable } from 'rxjs';
import {Finance, UserDTO} from "../../../generated";
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class FinanceService {

  constructor(private http: HttpClient) { }

  getFinances(user: UserDTO): Observable<Finance[]> {
    return this.http.get<Finance[]>(`${environment.api_url}/`+ user.id +'/finance');
  }

  addFinance(user: UserDTO, finance: Finance, options?: any): Observable<HttpEvent<boolean>> {
    return this.http.post<boolean>(`${environment.api_url}/`+ user.id +'/finance', finance, options);
  }

  updateFinance(user: UserDTO, financeData: Finance, options?: any): Observable<HttpEvent<boolean>> {
    return this.http.put<boolean>(`${environment.api_url}/${user.id}/finance/${financeData.id}`, financeData, options);
  }

  deleteFinance(user: UserDTO, financeId: number, options?: any): Observable<HttpEvent<void>>{
    return this.http.delete<void>(`${environment.api_url}/`+ user.id +'/finance/' + financeId, options);
  }
}
