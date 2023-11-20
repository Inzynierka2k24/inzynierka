import { Component } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Store} from "@ngrx/store";
import ReservationActions from "../store/reservation.actions";
import {selectCurrentUser} from "../../core/store/user/user.selectors";
import {switchMap} from "rxjs/operators";
import {Apartment, Reservation, UserDTO} from "../../../generated";
import {EMPTY, Observable} from "rxjs";
import { ReservationService } from "../services/reservation.service";
import {MessageService} from "primeng/api";
import {AppState} from "../../core/store/app.store";
import {ApartmentService} from "../../apartment/services/apartment.service";

@Component({
  selector: 'app-add-reservation',
  templateUrl: './add-reservation.component.html',
  styleUrls: ['./add-reservation.component.scss']
})
export class AddReservationComponent {
  isUserLoggedIn = false;
  addReservationForm;
  user: UserDTO;
  reservationResult$: Observable<string | undefined>;
  apartments: Apartment[] = [];

  constructor(private formBuilder: FormBuilder,
              private store: Store<AppState>,
              private reservationService: ReservationService,
              private apartmentService: ApartmentService,
              private messageService: MessageService){

    this.addReservationForm = formBuilder.nonNullable.group(
      {
        apartmentId: ['', [Validators.required]],
        startDate: ['', [Validators.required]],
        endDate: ['', [Validators.required]],
      })
  }

  addReservation(): void {
    if (this.addReservationForm.valid) {
      this.store
        .select(selectCurrentUser)
        .pipe(
          switchMap((user) => {
            if (!user) {
              this.isUserLoggedIn = false;
              throw new Error('User not logged in');
            }
            this.apartmentService.getApartments(user).subscribe((data: Apartment[]) => {
              this.apartments = data;
            });
            this.isUserLoggedIn = true;
            this.user = user;
            const reservationData: Reservation = {
              apartmentId: parseInt(this.addReservationForm.value.apartmentId!),
              startDate: new Date(this.addReservationForm.value.startDate!),
              endDate: new Date(this.addReservationForm.value.endDate!),
            };
            return this.reservationService.addReservation(this.user, reservationData.apartmentId, reservationData);
          })
        )
        .subscribe(
          {
            //todo display message
          next: response =>{
            this.addReservationForm.reset();
            this.messageService.add({
              severity: 'success',
              summary: 'Reservation added correctly',
              detail: '',
            })},
          error:error => {
              console.error('API call error:', error);
            }
          },
        );
    } else {
      this.messageService.add({
        severity: 'error',
        summary: 'Validation Error',
        detail: 'Please fill in all required fields and correct validation errors.',
      });
      this.markAllFieldsAsTouched(this.addReservationForm);
    }
  }

  private markAllFieldsAsTouched(formGroup: FormGroup): void {
    Object.keys(formGroup.controls).forEach((controlName) => {
      const control = formGroup.get(controlName);
      if (control instanceof FormGroup) {
        this.markAllFieldsAsTouched(<FormGroup<any>>control);
      } else {
        control?.markAsTouched();
      }
    });
  }
}
