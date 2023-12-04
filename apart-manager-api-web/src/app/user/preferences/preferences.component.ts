import { Component, OnDestroy, OnInit } from '@angular/core';
import { AppState } from '../../core/store/app.store';
import { Store } from '@ngrx/store';
import { FormBuilder, Validators } from '@angular/forms';
import { selectCurrentUser } from '../../core/store/user/user.selectors';
import UserActions from '../../core/store/user/user.actions';
import { Observable, Subscription } from 'rxjs';
import { ExternalAccount, UserDTO } from '../../../generated';
import { ConfirmationService } from 'primeng/api';
import { ExternalAccountsService } from '../services/external-accounts.service';

interface PreferencesCategory {
  title: string;
  rows: PreferencesTableRow[];
}

interface PreferencesTableRow {
  label: string;
  selector: string;
  inputType: string;
  dropdownValues?: string[];
}

@Component({
  selector: 'app-preferences',
  templateUrl: './preferences.component.html',
  styleUrls: ['./preferences.component.scss'],
})
export class PreferencesComponent implements OnInit, OnDestroy {
  readonly tableStructure: PreferencesCategory[] = [
    {
      title: 'ACCOUNT',
      rows: [
        { label: 'Username', selector: 'login', inputType: 'login' },
        { label: 'E-mail', selector: 'emailAddress', inputType: 'text' },
        {
          label: 'Password',
          selector: 'password',
          inputType: 'password',
        },
      ],
    },
    {
      title: 'NOTIFICATIONS',
      rows: [
        { label: 'SMS', selector: 'smsNotifications', inputType: 'checkbox' },
        {
          label: 'EMAIL',
          selector: 'emailNotifications',
          inputType: 'checkbox',
        },
      ],
    },
    {
      title: 'MEMBERSHIP',
      rows: [
        {
          label: 'Level',
          selector: 'level',
          inputType: 'dropdown',
          dropdownValues: ['FREE', 'PREMIUM', 'ENTERPRISE'],
        },
        {
          label: 'Billing',
          selector: 'billingType',
          inputType: 'dropdown',
          dropdownValues: ['CARD', 'CASH'],
        },
      ],
    },
  ];

  editable = false;
  userEditForm = this.formBuilder.nonNullable.group({
    login: ['', Validators.required],
    emailAddress: ['', [Validators.email]],
    password: ['', Validators.minLength(5)],
    level: [''],
    billingType: [''],
    smsNotifications: [false],
    emailNotifications: [false],
  });

  data: Map<string, any> = new Map<string, any>();

  subscriptions: Subscription[] = [];

  currentUser?: UserDTO;
  externalAccounts$: Observable<ExternalAccount[]>;
  showExternalAccountModal: boolean;
  editableModal: boolean = true;
  chosenAccount: ExternalAccount | undefined;

  constructor(
    private store: Store<AppState>,
    private formBuilder: FormBuilder,
    private confirmationService: ConfirmationService,
    private externalAccountsService: ExternalAccountsService,
  ) {}

  submitEditRequest() {
    if (this.userEditForm.valid && this.currentUser) {
      const editUserRequest = {
        username: this.userEditForm.value.login!,
        emailAddress: this.userEditForm.value.emailAddress!,
        password: this.userEditForm.value.password!,
      };
      this.store.dispatch(
        UserActions.edit({ userId: this.currentUser.id, editUserRequest }),
      );
    }
  }

  ngOnInit(): void {
    const dataSub = this.store.select(selectCurrentUser).subscribe((user) => {
      this.data = new Map(Object.entries({ ...user }));
      if (user) {
        this.userEditForm.setValue({
          login: user.login ?? '',
          emailAddress: user.emailAddress ?? '',
          password: '',
          level: user.level ?? '',
          billingType: user.billingType ?? '',
          smsNotifications: user.smsNotifications,
          emailNotifications: user.emailNotifications,
        });
      }
      this.currentUser = user;
      if (user)
        this.externalAccounts$ = this.externalAccountsService.get(user.id);
    });
    this.subscriptions.push(dataSub);
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach((sub) => sub.unsubscribe());
  }

  deleteUser() {
    this.confirmationService.confirm({
      message: 'Do you want to delete this account?',
      header: 'Delete Confirmation',
      icon: 'pi pi-info-circle',
      accept: () => {
        this.store.dispatch(UserActions.deleteUser());
      },
    });
  }

  openAddExternalAccountModal() {
    this.editableModal = false;
    this.showExternalAccountModal = true;
  }

  openEditExternalAccountModal(account: ExternalAccount) {
    this.chosenAccount = account;
    this.editableModal = true;
    this.showExternalAccountModal = true;
  }

  deleteExternalAccount(account: ExternalAccount) {
    if (this.currentUser?.id && account.id) {
      this.externalAccountsService
        .delete(this.currentUser.id, account.id)
        .subscribe(() => {
          this.externalAccounts$ = this.externalAccountsService.get(
            this.currentUser!.id,
          );
        });
    }
  }

  propagateData() {}

  openBillingPage() {}
}
