import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AppState } from '../../../../core/store/app.store';
import { Store } from '@ngrx/store';
import UserActions from '../../../../core/store/user/user.actions';

@Component({
  selector: 'app-user-card',
  templateUrl: './user-card.component.html',
  styleUrls: ['./user-card.component.scss'],
})
export class UserCardComponent {
  menuItems = [
    {
      label: 'Settings',
      icon: 'pi pi-fw pi-cog',
      command: () => {
        this.router.navigate(['user', 'settings']);
      },
    },
    {
      label: 'Log Out',
      icon: 'pi pi-fw pi-sign-out',
      command: () => {
        this.store.dispatch(UserActions.logout());
      },
    },
  ];

  constructor(
    private store: Store<AppState>,
    private router: Router,
  ) {}
}
