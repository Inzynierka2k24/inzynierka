import { Component, OnInit } from '@angular/core';
import { ReservationService } from '../services/reservation.service';
import { Apartment, Reservation, UserDTO } from '../../../generated';
import { ApartmentService } from '../../apartment/services/apartment.service';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';
import { Message, MessageService } from 'primeng/api';
import { Router } from '@angular/router';
import { selectCurrentUser } from '../../core/store/user/user.selectors';
import { AppState } from '../../core/store/app.store';
import { switchMap } from 'rxjs/operators';

@Component({
  selector: 'app-reservation-list',
  templateUrl: './reservation-list.component.html',
  styleUrls: ['./reservation-list.component.scss'],
})
export class ReservationListComponent implements OnInit {
  messages: Message[] = [];
  reservations: Reservation[] = [];
  reservation: Reservation;
  isUserLoggedIn = false;
  apartments: Apartment[] = [];
  apartment: Apartment;
  user$: Observable<UserDTO | undefined>;
  user: UserDTO;

  constructor(
    private store: Store<AppState>,
    private reservationService: ReservationService,
    private apartmentService: ApartmentService,
    private messageService: MessageService,
    private router: Router,
  ) {}

  ngOnInit() {
    this.fetchData();
  }

  addReservation() {
    this.router.navigate(['/reservations/add']);
  }

  editReservation(reservation: any) {
    const selectedApartment = this.getApartmentLabelById(reservation.apartmentId);
    const reservationWithApartmentLabel =
      { ...reservation, apartment: selectedApartment };

    this.router.navigate(['/reservations/edit', reservationWithApartmentLabel]);
  }

  propagateReservation(reservation: any) {
    this.store
      .select(selectCurrentUser)
      .pipe(
        switchMap((user) => {
          if (!user) {
            throw new Error('User not logged in');
          }
          this.user = user;
          return this.reservationService.propagateReservation(this.user,
            reservation.apartmentId,
            reservation,
            { responseType: 'text' });
        })
      )
      .subscribe(
        {
        next: response =>{
          this.messageService.add({
            severity: 'success',
            summary: 'Reservation propagated correctly',
            detail: 'success',
          });
          this.fetchData();
        },
        error:error => {
            console.error('API call error:', error);
            this.fetchData();
          }
        },
      );
  }

  deleteReservation(reservation: Reservation) {
    this.store
      .select(selectCurrentUser)
      .pipe(
        switchMap((user) => {
          if (!user) {
            throw new Error('User not logged in');
          }
          this.user = user;
          return this.reservationService.deleteReservation(
            this.user,
            reservation.apartmentId,
            <number>reservation.id,
            { responseType: 'text' },
          );
        }),
      )
      .subscribe({
        next: (response) => {
          this.messageService.add({
            severity: 'success',
            summary: 'Reservation deleted correctly',
            detail: 'success',
          });
          this.fetchData();
        },
        error: (error) => {
          console.error('API call error:', error);
          this.fetchData();
        },
      });
  }

  cleanMessages() {
    setTimeout(() => {
      this.messages = [];
    }, 3000);
  }

  fetchData() {
    this.user$ = this.store.select(selectCurrentUser);
    this.user$.subscribe((user) => {
      if (user) {
        this.apartmentService
          .getApartments(user)
          .subscribe((data: Apartment[]) => {
            this.apartments = data;

            if (this.apartments && this.apartments.length > 0) {
              this.reservations = [];
              for (const val of this.apartments) {
                this.reservationService
                  .getReservations(user, val)
                  .subscribe((data: Reservation[]) => {
                    this.reservations = this.reservations.concat(data);
                  });
              }
            }
          });
      }
    });
  }

  fetchReservations() {
    for (const apart of this.apartments){
      this.store
        .select(selectCurrentUser)
        .pipe(
          switchMap((user) => {
            if (!user) {
              throw new Error('User not logged in');
            }
            this.user = user;
            const today = new Date();
            const nextYear = new Date(today);
            nextYear.setFullYear(today.getFullYear() + 1);
            const requestBody = {
                from: today.toISOString().split('T')[0],
                to: nextYear.toISOString().split('T')[0],
            };

            return this.reservationService.fetchReservations(
              this.user,
              <number>apart.id,
              { responseType: 'text' });
          })
        )
        .subscribe(
          {
          next: response =>{
            this.messageService.add({
              severity: 'success',
              summary: 'Reservation fetched correctly',
              detail: 'success',
            });
            this.fetchData();
          },
          error:error => {
              console.error('API call error:', error);
              this.fetchData();
            }
          },
        );
    }
  }

  getApartmentLabelById(id: number) {
    const apartment = this.apartments.find(ap => ap.id === id);
    return apartment?.title + ', ' + apartment?.city;
  }
}
