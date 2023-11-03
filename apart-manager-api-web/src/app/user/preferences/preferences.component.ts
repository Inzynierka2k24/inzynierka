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
  placeholder?: string;
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
        { label: 'E-mail', selector: 'emailAddress' },
        { label: 'Password', selector: 'password', placeholder: '*******' },
      ],
    },
    {
      title: 'MEMBERSHIP',
      rows: [
        {
          label: 'Level',
          selector: 'level',
        },
        { label: 'Billing', selector: 'billingType' },
      ],
    },
    {
      title: 'NOTIFICATIONS',
      rows: [{ label: 'SMS', selector: 'sms' }],
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
        UserActions.edit({ user: this.currentUser.login, editUserRequest }),
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
}
