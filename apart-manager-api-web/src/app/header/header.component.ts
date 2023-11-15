import {Component, ViewChild} from '@angular/core';
import {Router} from "@angular/router";
import {MenuItem} from "primeng/api";
import {SlideMenu} from "primeng/slidemenu";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss'],
})
export class HeaderComponent {
  constructor(private router: Router) {}
  apartmentItems: MenuItem[];
  reservationItems: MenuItem[];
  financeItems: MenuItem[];

  @ViewChild('apartmentMenu') apartmentMenu!: SlideMenu;
  @ViewChild('reservationMenu') reservationMenu!: SlideMenu;
  @ViewChild('financeMenu') financeMenu!: SlideMenu;

  ngOnInit() {
      this.apartmentItems = [
          {
            label: 'Apartments List',
            command: () => this.router.navigate(['/apartments']),
          },
          {
            label: 'Add apartment',
            command: () => this.router.navigate(['/apartments/add']),
          },
      ];
      this.reservationItems = [
          {
            label: 'Reservations Calendar',
            command: () => this.router.navigate(['/reservations/calendar']),
          },
          {
            label: 'Reservations List',
            command: () => this.router.navigate(['/reservations']),
          },
          {
            label: 'Add reservation',
            command: () => this.router.navigate(['/reservations/add']),
          },
      ];
      this.financeItems = [
          {
            label: 'Finances List',
            command: () => this.router.navigate(['/finances']),
          },
          {
            label: 'Add finance',
            command: () => this.router.navigate(['/finances/add']),
          },
          {
            label: 'Finance Chart',
            command: () => this.router.navigate(['/finances/chart']),
          },
      ];
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
