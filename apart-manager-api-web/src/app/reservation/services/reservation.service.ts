import { Injectable } from '@angular/core';
import { HttpClient, HttpEvent } from '@angular/common/http';
import { Observable } from 'rxjs';
import {
  Apartment,
  Reservation,
  ReservationDTO,
  UserDTO,
} from '../../../generated';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class ReservationService {
  constructor(private http: HttpClient) {}

  getReservations(
    user: UserDTO,
    apartment: Apartment,
  ): Observable<Reservation[]> {
    return this.http.get<Reservation[]>(
      `${environment.api_url}/` +
        user.id +
        '/apartment/' +
        apartment.id +
        '/reservation',
    );
  }

  getReservationDTOs(
    userId: number,
    apartmentId: number,
  ): Observable<ReservationDTO> {
    return this.http.get<ReservationDTO>(
      `${environment.api_url}/` + userId + '/apartment/' + apartmentId + '/dto',
    );
  }

  addReservation(
    user: UserDTO,
    apartmentId: number,
    reservation: Reservation,
    options?: any,
  ): Observable<HttpEvent<boolean>> {
    return this.http.post<boolean>(
      `${environment.api_url}/` +
        user.id +
        '/apartment/' +
        apartmentId +
        '/reservation',
      reservation,
      options,
    );
  }

  updateReservation(
    user: UserDTO,
    apartmentId: number,
    reservation: Reservation,
  ): Observable<Reservation> {
    return this.http.put<Reservation>(
      `${environment.api_url}/` +
        user.id +
        '/apartment/' +
        apartmentId +
        '/reservation/' +
        reservation.id,
      reservation,
    );
  }

  deleteReservation(
    user: UserDTO,
    apartmentId: number,
    reservationId: number,
    options?: any,
  ): Observable<HttpEvent<void>> {
    return this.http.delete<void>(
      `${environment.api_url}/` +
        user.id +
        '/apartment/' +
        apartmentId +
        '/reservation/' +
        reservationId,
      options,
    );
  }

  propagateReservation(
    userId: number,
    apartmentId: number,
    reservationId: number,
  ) {
    return this.http.get<Map<string, string>>(
      `${environment.api_url}/` +
        userId +
        '/external/integration//propagate/apartment/' +
        apartmentId +
        '/reservation/' +
        reservationId,
    );
  }
}
