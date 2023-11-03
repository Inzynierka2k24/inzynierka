import { NgModule } from '@angular/core';
import { ReservationListComponent } from './reservation-list/reservation-list.component';
import { SharedModule } from '../core/shared.module';
import { FullCalendarModule } from '@fullcalendar/angular';

@NgModule({
  declarations: [ReservationListComponent],
  imports: [SharedModule, FullCalendarModule],
})
export class ReservationsModule {}
