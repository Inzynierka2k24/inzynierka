import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import {
  ExternalAccount,
  ExternalService,
  UserDTO,
} from '../../../../generated';
import { ExternalAccountsService } from '../../services/external-accounts.service';
import { AppState } from '../../../core/store/app.store';
import { Store } from '@ngrx/store';
import { selectCurrentUser } from '../../../core/store/user/user.selectors';

@Component({
  selector: 'app-add-external-account-modal',
  templateUrl: './add-external-account-modal.component.html',
  styleUrls: ['./add-external-account-modal.component.scss'],
})
export class AddExternalAccountModalComponent {
  @Input()
  visible: boolean;
  @Output()
  visibleChange = new EventEmitter<boolean>();

  currentUser: UserDTO | undefined;

  addExternalAccountForm: FormGroup;
  serviceTypes: ExternalService[] = [
    'BOOKING',
    'AIRBNB',
    'TRIVAGO',
    'NOCOWANIEPL',
  ];

  constructor(
    private formBuilder: FormBuilder,
    private externalAccountsService: ExternalAccountsService,
    private store: Store<AppState>,
  ) {
    this.addExternalAccountForm = this.formBuilder.group({
      serviceType: ['', Validators.required],
      login: ['', Validators.required],
      password: ['', Validators.required],
    });
    store.select(selectCurrentUser).subscribe((user) => {
      this.currentUser = user;
    });
  }

  addExternalAccount() {
    if (this.addExternalAccountForm.valid && this.currentUser?.id) {
      const newAccount: ExternalAccount = {
        serviceType: this.addExternalAccountForm.value.serviceType,
        login: this.addExternalAccountForm.value.login,
        password: this.addExternalAccountForm.value.password,
      };
      this.externalAccountsService
        .add(this.currentUser.id, newAccount)
        .subscribe(() => {
          this.visibleChange.emit(false);
        });
    }
  }
}
