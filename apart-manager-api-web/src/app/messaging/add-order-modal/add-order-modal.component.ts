import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { AppState } from '../../core/store/app.store';
import { Store } from '@ngrx/store';
import { mergeMap, Observable } from 'rxjs';
import { Apartment, ContactDTO, UserDTO } from '../../../generated';
import { ApartmentService } from '../../apartment/services/apartment.service';
import { selectCurrentUser } from '../../core/store/user/user.selectors';
import { FormGroup, NonNullableFormBuilder, Validators } from '@angular/forms';
import { MessagingService } from '../messaging.service';
import MessagingActions from '../../core/store/messaging/messaging.actions';

@Component({
  selector: 'app-add-order-modal',
  templateUrl: './add-order-modal.component.html',
  styleUrls: ['./add-order-modal.component.scss'],
})
export class AddOrderModalComponent implements OnInit {
  @Input()
  contact: ContactDTO | undefined;
  @Input()
  visible: boolean;
  @Output()
  visibleChange = new EventEmitter<boolean>();

  readonly intervalTypes = ['HOURS', 'DAYS', 'WEEKS'];
  readonly triggerTypes = ['RESERVATION', 'CHECKIN', 'CHECKOUT'];

  apartments$: Observable<Apartment[]>;
  addOrderForm: FormGroup;

  currentUser: UserDTO;

  constructor(
    private store: Store<AppState>,
    private apartmentService: ApartmentService,
    private messagingService: MessagingService,
    private formBuilder: NonNullableFormBuilder,
  ) {
    this.addOrderForm = this.formBuilder.group({
      apartments: [[], [Validators.required, Validators.minLength(1)]],
      triggerType: [this.triggerTypes[0], Validators.required],
      intervalType: [this.intervalTypes[0], Validators.required],
      intervalValue: [0, [Validators.required, Validators.min(1)]],
      message: ['', [Validators.maxLength(255)]],
    });
  }

  ngOnInit(): void {
    this.apartments$ = this.store.select(selectCurrentUser).pipe(
      mergeMap((user) => {
        if (user) {
          this.currentUser = user;
          return this.apartmentService.getApartments(user);
        }
        return [];
      }),
    );
  }

  sendOrder() {
    if (this.addOrderForm.valid && this.contact?.id && this.currentUser.id) {
      const message = {
        apartments: this.addOrderForm.controls['apartments'].value,
        triggerType: this.addOrderForm.controls['triggerType'].value,
        intervalType: this.addOrderForm.controls['intervalType'].value,
        intervalValue: this.addOrderForm.controls['intervalValue'].value,
        message: this.addOrderForm.controls['message'].value,
        contact: this.contact,
      };
      this.store.dispatch(
        MessagingActions.addOrder({
          userId: this.currentUser.id,
          contactId: this.contact.id,
          message,
        }),
      );
    }
  }
}
