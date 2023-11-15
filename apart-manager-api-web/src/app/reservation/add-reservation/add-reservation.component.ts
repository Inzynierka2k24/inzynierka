import { Component } from '@angular/core';
import {FormBuilder, Validators} from "@angular/forms";
import {Store} from "@ngrx/store";
import ReservationActions from "../store/reservation.actions";

@Component({
  selector: 'app-add-reservation',
  templateUrl: './add-reservation.component.html',
  styleUrls: ['./add-reservation.component.scss']
})
export class AddReservationComponent {
  addReservationForm;

  constructor(private formBuilder: FormBuilder, private store: Store){
    this.addReservationForm = formBuilder.nonNullable.group(
      {
        apartmentId: ['', [Validators.required]],
        startDate: ['', [Validators.required]],
        endDate: ['', [Validators.required]],
      })
  }

  addReservation(): void {
    if (this.addReservationForm.valid){
      this.store.dispatch(
        ReservationActions.addReservation({
          apartmentId: parseInt(this.addReservationForm.value.apartmentId!),
          startDate: new Date(this.addReservationForm.value.startDate!),
          endDate: new Date(this.addReservationForm.value.endDate!),
        }),
      )
      this.addReservationForm.reset();
    }
    else {
      //TODO validation errors
    }
  }
}
