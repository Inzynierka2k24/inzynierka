import { Injectable } from '@angular/core';
import {HttpClient, HttpEvent} from '@angular/common/http';
import { Observable } from 'rxjs';
import {Apartment} from "../../../generated";
import {UserDTO} from "../../../generated";
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ApartmentService {

  constructor(private http: HttpClient) { }

  getApartments(user: UserDTO): Observable<Apartment[]> {
    return this.http.get<Apartment[]>(`${environment.api_url}/`+ user.id +'/apartment');
  }

  addApartment(user: UserDTO, apartment: Apartment, options?: any): Observable<HttpEvent<string>> {
    return this.http.post<string>(`${environment.api_url}/`+ user.id +'/apartment', apartment, options);
  }

  updateApartment(user: UserDTO, apartment: Apartment): Observable<Apartment> {
    return this.http.put<Apartment>(`${environment.api_url}/`+ user.id +'/apartment/' + apartment.id, apartment);
  }

  deleteApartment(user: UserDTO, apartmentId: number, options?: any): Observable<HttpEvent<void>> {
    return this.http.delete<void>(`${environment.api_url}/`+ user.id +'/apartment/' + apartmentId, options);
  }
}
