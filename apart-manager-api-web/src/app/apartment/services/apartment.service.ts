import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {Apartment} from "../../../generated";
import {UserDTO} from "../../../generated";

@Injectable({
  providedIn: 'root'
})
export class ApartmentService {

  constructor(private http: HttpClient) { }

  getApartments(user: UserDTO): Observable<Apartment[]> {
    return this.http.get<Apartment[]>('/api/'+ user.id +'/apartment');
  }

  addApartment(user: UserDTO, apartment: Apartment): Observable<boolean> {
    return this.http.post<boolean>('/api/'+ user.id +'/apartment', apartment);
  }

  updateApartment(user: UserDTO, apartment: Apartment): Observable<Apartment> {
    return this.http.put<Apartment>('/api/'+ user.id +'/apartment' + apartment.id, apartment);
  }

  deleteApartment(user: UserDTO, apartmentId: number): Observable<void> {
    return this.http.delete<void>('/api/'+ user.id +'/apartment' + apartmentId);
  }
}
