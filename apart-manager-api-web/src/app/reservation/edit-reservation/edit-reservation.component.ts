import { Component } from '@angular/core';
import {Message, MessageService} from "primeng/api";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {ReservationService} from "../../reservation/services/reservation.service";
import {ActivatedRoute, Router} from "@angular/router";
import {Reservation} from "../../../generated";

@Component({
  selector: 'app-edit-reservation',
  templateUrl: './edit-reservation.component.html',
  styleUrls: ['./edit-reservation.component.scss']
})
export class EditReservationComponent {
  reservation: any;
  messages: Message[] = [];
  editReservationForm: FormGroup;


  constructor(
    private reservationService: ReservationService,
    private messageService: MessageService,
    private router: Router,
    private fb: FormBuilder,
    private route: ActivatedRoute
  ) {
    this.route.params.subscribe((params) => {
      this.reservation = params;
      this.editReservationForm = this.fb.group({
        apartmentId: [params['apartmentId'], Validators.required],
        startDate: [params['startDate'], Validators.required],
        endDate: [params['endDate'], Validators.required]
      });
    });
  }

  addReservation() {
    this.router.navigate(['/reservations/add']);
  }

  saveChanges(reservation: Reservation) {
    const updatedReservation: Reservation = this.editReservationForm.value;

    // this.reservationService.updateReservation(updatedReservation).subscribe(
    //   (response) => {
    //     this.messages = [
    //       {
    //         severity: 'success',
    //         detail: 'Reservation edited successfully',
    //       }
    //     ];
    //     this.cleanMessages();
    //     this.router.navigate(['/reservations']);
    //   },
    //   (error) => {
    //     this.messages = [
    //       {
    //         severity: 'warn',
    //         detail: 'Cannot update reservation',
    //       }
    //     ];
    //     this.cleanMessages();
    //   }
    // );
  }

  cancelEditing() {
    this.router.navigate(['/reservations']);
  }

  cleanMessages() {
    setTimeout(() => {
      this.messages = [];
    }, 3000);
  }
}
