import {Component} from '@angular/core';
import {Message, MessageService} from "primeng/api";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {ReservationService} from "../../reservation/services/reservation.service";
import {ActivatedRoute, Router} from "@angular/router";
import {Reservation, UserDTO} from "../../../generated";
import {selectCurrentUser} from "../../core/store/user/user.selectors";
import {Observable} from "rxjs";
import {Store} from "@ngrx/store";
import {AppState} from "../../core/store/app.store";
import {switchMap} from "rxjs/operators";

@Component({
  selector: 'app-edit-reservation',
  templateUrl: './edit-reservation.component.html',
  styleUrls: ['./edit-reservation.component.scss']
})
export class EditReservationComponent {
  reservation: any;
  messages: Message[] = [];
  apartmentLabel: string;
  editReservationForm: FormGroup;
  user: UserDTO;
  user$: Observable<UserDTO | undefined>;
  isUserLoggedIn = false;

  constructor(
    private reservationService: ReservationService,
    private messageService: MessageService,
    private store: Store<AppState>,
    private router: Router,
    private fb: FormBuilder,
    private route: ActivatedRoute
  ) {
    this.route.params.subscribe((params) => {
      this.reservation = params;
      this.apartmentLabel = this.reservation.apartment!;

      this.editReservationForm = this.fb.group({
        startDate: [this.convertDate(this.reservation.startDate), Validators.required],
        endDate: [this.convertDate(this.reservation.endDate), Validators.required]
      });
    });
  }

  convertDate(date: string) {
    return new Date(date);
  }

  saveChanges(): void {
    if (this.editReservationForm.valid) {
      this.store
      .select(selectCurrentUser)
      .pipe(
        switchMap((user) => {
          if (!user) {
            this.isUserLoggedIn = false;
            throw new Error('User not logged in');
          }
          this.isUserLoggedIn = true;
          this.user = user;
          const reservationData: Reservation = {
            id: this.reservation.id!,
            apartmentId: this.reservation.apartmentId!,
            startDate: this.editReservationForm.value.startDate,
            endDate: this.editReservationForm.value.endDate,
          };
          return this.reservationService.updateReservation(
            this.user,
            reservationData.apartmentId,
            reservationData,
            { responseType: 'text' }
          );
        }),
      )
      .subscribe({
        next: () => {
          this.messageService.add({
            severity: 'success',
            summary: 'Reservation updated'
          });
          this.router.navigate(['/reservations']);
        },
        error: (error) => {
          console.error('API call error:', error);
        },
      });
    } else {
      this.messageService.add({
        severity: 'error',
        summary: 'Validation Error',
        detail:
          'Please fill in all required fields and correct validation errors.',
      });
    }
  }

  cancelEditing() {
    window.history.back();
  }

  cleanMessages() {
    setTimeout(() => {
      this.messages = [];
    }, 3000);
  }
}
