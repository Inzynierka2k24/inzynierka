import { Component, OnDestroy, OnInit } from '@angular/core';
import { AppState } from '../../core/store/app.store';
import { Store } from '@ngrx/store';
import { FormBuilder, Validators } from '@angular/forms';
import { selectCurrentUser } from '../../core/store/user/user.selectors';
import UserActions from '../../core/store/user/user.actions';
import { Subscription } from 'rxjs';
import { UserDTO } from '../../../generated';

interface PreferencesCategory {
  title: string;
  rows: PreferencesTableRow[];
}

interface PreferencesTableRow {
  label: string;
  selector: string;
  inputType: string;
  placeholder?: string;
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
        { label: 'E-mail', selector: 'emailAddress', inputType: 'text' },
        {
          label: 'Password',
          selector: 'password',
          placeholder: '*******',
          inputType: 'password',
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
    {
      title: 'NOTIFICATIONS',
      rows: [
        { label: 'SMS', selector: 'smsNotifications', inputType: 'checkbox' },
      ],
    },
  ];

  editable = false;
  userEditForm = this.formBuilder.nonNullable.group({
    emailAddress: ['', [Validators.email]],
    password: ['', Validators.minLength(5)],
    level: [''],
    billingType: [''],
    smsNotifications: [false],
  });

  data: Map<string, any> = new Map<string, any>();

  subscriptions: Subscription[] = [];

  currentUser?: UserDTO;

  constructor(
    private store: Store<AppState>,
    private formBuilder: FormBuilder,
  ) {}

  submitEditRequest() {
    if (this.userEditForm.valid && this.currentUser) {
      const editUserRequest = {
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
          emailAddress: user.emailAddress ?? '',
          password: '**********',
          level: user.level ?? '',
          billingType: user.billingType ?? '',
          smsNotifications: user.smsNotifications,
        });
      }
      this.currentUser = user;
    });
    this.subscriptions.push(dataSub);
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach((sub) => sub.unsubscribe());
  }

  deleteUser() {
    this.store.dispatch(UserActions.deleteUser());
  }
}
