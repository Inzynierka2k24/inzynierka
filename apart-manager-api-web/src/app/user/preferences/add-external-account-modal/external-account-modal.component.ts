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
  selector: 'app-external-account-modal',
  templateUrl: './external-account-modal.component.html',
  styleUrls: ['./external-account-modal.component.scss'],
})
export class ExternalAccountModalComponent {
  @Input()
  visible: boolean;
  @Output()
  visibleChange = new EventEmitter<boolean>();
  currentUser: UserDTO | undefined;
  externalAccountForm: FormGroup;
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
    this.externalAccountForm = this.formBuilder.group({
      serviceType: ['', Validators.required],
      login: ['', Validators.required],
      password: ['', Validators.required],
    });
    store.select(selectCurrentUser).subscribe((user) => {
      this.currentUser = user;
    });
  }

  private _editedAccount: ExternalAccount | undefined;

  get editedAccount(): ExternalAccount | undefined {
    return this._editedAccount;
  }

  @Input()
  set editedAccount(account: ExternalAccount | undefined) {
    this._editedAccount = account;
    this.externalAccountForm.patchValue({
      login: this.editedAccount?.login ?? '',
      password: this.editedAccount?.password ?? '',
    });
  }

  private _edit = false;

  get edit(): boolean {
    return this._edit;
  }

  @Input()
  set edit(value: boolean) {
    this._edit = value;
    if (value) {
      this.externalAccountForm.controls['serviceType'].disable();
    } else {
      this.externalAccountForm.controls['serviceType'].enable();
    }
  }

  submit() {
    if (this.edit) {
      this.editExternalAccount();
    } else {
      this.addExternalAccount();
    }
  }

  addExternalAccount() {
    if (this.externalAccountForm.valid && this.currentUser?.id) {
      const newAccount: ExternalAccount = {
        serviceType: this.externalAccountForm.value.serviceType,
        login: this.externalAccountForm.value.login,
        password: this.externalAccountForm.value.password,
      };
      this.externalAccountsService
        .add(this.currentUser.id, newAccount)
        .subscribe(() => {
          this.visibleChange.emit(false);
        });
    }
  }

  resetForm() {
    this.externalAccountForm.reset();
    this.editedAccount = undefined;
  }

  editExternalAccount() {
    if (
      this.externalAccountForm.valid &&
      this.currentUser?.id &&
      this.editedAccount
    ) {
      const newAccount: ExternalAccount = {
        ...this.editedAccount,
        login: this.externalAccountForm.value.login,
        password: this.externalAccountForm.value.password,
      };
      this.externalAccountsService
        .edit(this.currentUser.id, newAccount)
        .subscribe(() => {
          this.visibleChange.emit(false);
        });
    }
  }
}
