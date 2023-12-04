import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { MessagingService } from '../messaging.service';
import { UserDTO } from '../../../generated';
import { AppState } from '../../core/store/app.store';
import { Store } from '@ngrx/store';
import { selectCurrentUser } from '../../core/store/user/user.selectors';
import MessagingActions from '../../core/store/messaging/messaging.actions';
import { selectMessagingLoadingState } from '../../core/store/messaging/messaging.selectors';

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
  @Input()
  editedContact: any;
  isLoading$ = this.store.select(selectMessagingLoadingState);
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

  private _editable = false;

  get editable(): boolean {
    return this._editable;
  }

  @Input()
  set editable(edit: boolean) {
    this._editable = edit;
    if (edit && this.editedContact) {
      this.addContactForm.setValue({
        name: this.editedContact.name,
        contactType: this.editedContact.contactType,
        price: this.editedContact.price,
        mail: this.editedContact.mail,
        phone: this.editedContact.phone,
      });
    }
  }

  sendContact() {
    if (this.addContactForm.valid && this.currentUser) {
      const contact = {
        name: this.addContactForm.value.name,
        contactType: this.addContactForm.value.contactType,
        price: this.addContactForm.value.price,
        mail: this.addContactForm.value.mail,
        phone: this.addContactForm.value.phone,
        emailNotifications: !!this.addContactForm.value.email,
        smsNotifications: !!this.addContactForm.value.phone,
      };
      if (this.editable) {
        this.store.dispatch(
          MessagingActions.editContact({
            userId: this.currentUser.id,
            contact: { ...contact, id: this.editedContact.id },
          }),
        );
      } else {
        this.store.dispatch(
          MessagingActions.addContact({
            userId: this.currentUser.id,
            contact,
          }),
        );
      }

      this.isLoading$.subscribe((loading) => {
        if (!loading) {
          this.visibleChange.emit(false);
        }
      });
    }
  }
}
