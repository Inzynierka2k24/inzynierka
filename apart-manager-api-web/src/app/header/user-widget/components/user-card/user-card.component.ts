import { Component } from '@angular/core';
import { AppState } from '../../../../core/store/app.store';
import { Store } from '@ngrx/store';
import UserActions from '../../../../core/store/user/user.actions';

@Component({
  selector: 'app-user-card',
  templateUrl: './user-card.component.html',
  styleUrls: ['./user-card.component.scss'],
})
export class UserCardComponent {
  readonly menuItems = [
    {
      label: 'Dashboard',
      icon: 'pi pi-fw pi-chart-bar',
      routerLink: '/user/dashboard',
    },
    {
      label: 'Settings',
      icon: 'pi pi-fw pi-cog',
      routerLink: '/user/settings',
    },
    {
      label: 'Messaging',
      icon: 'pi pi-fw pi-envelope',
      routerLink: '/messaging',
    },
    {
      label: 'Log Out',
      icon: 'pi pi-fw pi-sign-out',
      command: () => {
        this.store.dispatch(UserActions.logout());
      },
    },
  ];

  constructor(private store: Store<AppState>) {}
}
