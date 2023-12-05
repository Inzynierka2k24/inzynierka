import { Component, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { MenuItem, MessageService } from 'primeng/api';
import { SlideMenu } from 'primeng/slidemenu';
import { selectCurrentUser } from '../core/store/user/user.selectors';
import { AppState } from '../core/store/app.store';
import { Store } from '@ngrx/store';
import { UserDTO } from '../../generated';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss'],
})
export class HeaderComponent {
  apartmentItems: MenuItem[];
  reservationItems: MenuItem[];
  financeItems: MenuItem[];
  @ViewChild('apartmentMenu') apartmentMenu!: SlideMenu;
  @ViewChild('reservationMenu') reservationMenu!: SlideMenu;
  @ViewChild('financeMenu') financeMenu!: SlideMenu;

  currentUser: UserDTO | undefined;

  constructor(
    public router: Router,
    private store: Store<AppState>,
    private messageService: MessageService,
  ) {}

  ngOnInit() {
    this.store.select(selectCurrentUser).subscribe((user) => {
      this.currentUser = user;
    });
    this.apartmentItems = [
      {
        label: 'Apartments List',
        command: () => this.navigateWithErrorHandling('/apartments'),
      },
      {
        label: 'Add apartment',
        command: () => this.navigateWithErrorHandling('/apartments/add'),
      },
      {
        label: 'External offers',
        command: () =>
          this.navigateWithErrorHandling('/apartments/externalOffers'),
      },
    ];
    this.reservationItems = [
      {
        label: 'Reservations Calendar',
        command: () => this.navigateWithErrorHandling('/reservations/calendar'),
      },
      {
        label: 'Reservations List',
        command: () => this.navigateWithErrorHandling('/reservations'),
      },
      {
        label: 'Add reservation',
        command: () => this.navigateWithErrorHandling('/reservations/add'),
      },
    ];
    this.financeItems = [
      {
        label: 'Finances List',
        command: () => this.navigateWithErrorHandling('/finances'),
      },
      {
        label: 'Add finance',
        command: () => this.navigateWithErrorHandling('/finances/add'),
      },
      {
        label: 'Finance Chart',
        command: () => this.navigateWithErrorHandling('/finances/chart'),
      },
    ];
  }

  navigateWithErrorHandling(route: string) {
    if (!this.currentUser) {
      this.messageService.add({
        severity: 'error',
        summary: 'Error',
        detail: 'You are not logged in!',
      });
    } else {
      this.router.navigate([route]);
    }
  }

  showClickApartments(event: any) {
    this.apartmentMenu.show(event);
  }

  showClickReservations(event: any) {
    this.reservationMenu.show(event);
  }

  showClickFinances(event: any) {
    this.financeMenu.show(event);
  }
}
