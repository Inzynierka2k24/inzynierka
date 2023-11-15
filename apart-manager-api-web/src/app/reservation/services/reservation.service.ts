import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {Apartment, Reservation, UserDTO} from "../../../generated";

@Injectable({
  providedIn: 'root'
})
export class ReservationService {

  constructor(private http: HttpClient) { }

  getReservations(user: UserDTO, apartment: Apartment): Observable<Reservation[]> {
    return this.http.get<Reservation[]>('/api/'+ user.id +'/apartment/' + apartment.id +'/reservation');
  }

  addReservation(user: UserDTO, reservation: Reservation): Observable<boolean> {
    return this.http.post<boolean>('/api/'+ user.id +'/reservation/', reservation);
  }

  updateReservation(user: UserDTO, reservation: Reservation): Observable<Reservation> {
    return this.http.put<Reservation>('/api/'+ user.id +'/reservation/' + reservation.id, reservation);
  }

  deleteReservation(user: UserDTO, reservationId: number): Observable<void> {
    return this.http.delete<void>('/api/'+ user.id +'/reservation/' + reservationId);
  }
}
