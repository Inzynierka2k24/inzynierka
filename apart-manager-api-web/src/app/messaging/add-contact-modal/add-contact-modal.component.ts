import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { MessagingService } from '../messaging.service';
import { UserDTO } from '../../../generated';
import { AppState } from '../../core/store/app.store';
import { Store } from '@ngrx/store';
import { selectCurrentUser } from '../../core/store/user/user.selectors';

@Component({
  selector: 'app-add-contact-modal',
  templateUrl: './add-contact-modal.component.html',
  styleUrls: ['./add-contact-modal.component.scss'],
})
export class AddContactModalComponent {
  @Input()
  visible: boolean;
  @Output()
  visibleChange = new EventEmitter<boolean>();

  currentUser: UserDTO | undefined;

  addContactForm: any;
  readonly contactTypes = ['CLEANING', 'MECHANIC', 'ELECTRICIAN', 'UNKNOWN'];

  constructor(
    private formBuilder: FormBuilder,
    private messagingService: MessagingService,
    private store: Store<AppState>,
  ) {
    this.addContactForm = formBuilder.group({
      name: ['', [Validators.required, Validators.minLength(3)]],
      contactType: ['', Validators.required],
      mail: ['', [Validators.required, Validators.email]],
      phone: ['', [Validators.required, Validators.minLength(8)]],
      price: [0, [Validators.required, Validators.min(0)]],
    });

    this.store
      .select(selectCurrentUser)
      .subscribe((user) => (this.currentUser = user));
  }

  sendContact() {
    if (this.addContactForm.valid && this.currentUser) {
      this.messagingService
        .addContact(this.currentUser.id, {
          name: this.addContactForm.value.name,
          contactType: this.addContactForm.value.contactType,
          price: this.addContactForm.value.price,
          mail: this.addContactForm.value.mail,
          phone: this.addContactForm.value.phone,
          emailNotifications: !!this.addContactForm.value.email,
          smsNotifications: !!this.addContactForm.value.phone,
        })
        .subscribe(() => {
          this.visibleChange.emit(false);
        });
    }
  }
}
