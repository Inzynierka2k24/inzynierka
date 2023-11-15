import { Component, OnInit } from '@angular/core';
import { ReservationService } from '../services/reservation.service';
import {Apartment, Reservation, UserDTO} from '../../../generated';
import { ApartmentService} from "../../apartment/services/apartment.service";
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';
import { MessageService } from 'primeng/api';
import { Message } from 'primeng/api';
import {Router} from "@angular/router";
import {selectCurrentUser} from "../../core/store/user/user.selectors";
import {AppState} from "../../core/store/app.store";

@Component({
  selector: 'app-reservation-list',
  templateUrl: './reservation-list.component.html',
  styleUrls: ['./reservation-list.component.scss']
})
export class ReservationListComponent implements OnInit {
  messages: Message[] = [];
  user: UserDTO;
  reservations: Reservation[] = [];
  reservation: Reservation;
  isUserLoggedIn = false;
  apartments: Apartment[] = [];
  apartment: Apartment;

  constructor(private store: Store<AppState>,
              private reservationService: ReservationService,
              private apartmentService: ApartmentService,
              private messageService: MessageService,
              private router: Router) {
  }

  ngOnInit() {
    const userDataSub = this.store
      .select(selectCurrentUser)
      .subscribe((user) => {
        if (user) {
          this.isUserLoggedIn = true;
          this.user = user;

          this.apartmentService.getApartments(this.user).subscribe((data: Apartment[]) => {
            this.apartments = data;

            if (this.apartments && this.apartments.length > 0) {
              for(const val of this.apartments) {
                this.reservationService.getReservations(this.user, val).subscribe((data: Reservation[]) => {
                  this.reservations = this.reservations.concat(data);
                });
              }
            }
          });
        } else {
          this.isUserLoggedIn = false;
        }
      });
  }

  addReservation() {
    this.router.navigate(['/reservations/add']);
  }

  editReservation(reservation: any) {
    this.router.navigate(['/reservations/edit', reservation]);

    // console.log('Edit Reservation:', event);
    // this.messages = [{
    //   severity: 'success',
    //   detail: 'Reservation edited successfully',
    // }];
    // this.cleanMessages();
  }

  deleteReservation(event: any) {
    console.log('Delete Reservation:', event);
    this.messages = [{
      severity: 'info',
      detail: 'Reservation deleted successfully',
    }];
    this.cleanMessages();
  }

  cleanMessages() {
    setTimeout(() => {
      this.messages = [];
    }, 3000);
  }
}