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
  ): Observable<ReservationDTO[]> {
    return this.http.get<ReservationDTO[]>(
      `${environment.api_url}/` +
        userId +
        '/apartment/' +
        apartmentId +
        '/reservation/dto',
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
    options?: any,
  ): Observable<HttpEvent<string>> {
    return this.http.put<string>(
      `${environment.api_url}/` +
        user.id +
        '/apartment/' +
        apartmentId +
        '/reservation',
      reservation,
      options,
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
    user: UserDTO,
    apartmentId: number,
    reservation: Reservation,
    options?: any,
  ): Observable<HttpEvent<boolean>> {
    return this.http.get<boolean>(
      `${environment.api_url}/` +
        user.id +
        '/external/integration/propagate/apartment/' +
        apartmentId +
        '/reservation/' +
        reservation.id,
      options,
    );
  }

  fetchReservations(user: UserDTO, apartmentId: number, requestBody: any) {
    return this.http.post(
      `${environment.api_url}/${user.id}/external/integration/apartment/${apartmentId}/get/reservations`,
      requestBody,
      { responseType: 'text' },
    );
  }
}
