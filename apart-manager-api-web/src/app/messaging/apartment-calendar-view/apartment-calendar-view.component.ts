import { Component, Input, OnInit } from '@angular/core';
import { Apartment, ReservationDTO } from '../../../generated';
import { ReservationService } from '../../reservation/services/reservation.service';
import { AppState } from '../../core/store/app.store';
import { Store } from '@ngrx/store';
import { selectCurrentUser } from '../../core/store/user/user.selectors';
import dayGridPlugin from '@fullcalendar/daygrid';
import interactionPlugin from '@fullcalendar/interaction';

@Component({
  selector: 'app-apartment-calendar-view',
  templateUrl: './apartment-calendar-view.component.html',
  styleUrls: ['./apartment-calendar-view.component.scss'],
})
export class ApartmentCalendarViewComponent implements OnInit {
  @Input() apartment: Apartment;
  calendarOptions: any;
  events: any[] = [];

  constructor(
    private reservationService: ReservationService,
    private store: Store<AppState>,
  ) {}

  ngOnInit(): void {
    this.store.select(selectCurrentUser).subscribe((user) => {
      if (user && this.apartment) {
        this.reservationService
          .getReservationDTOs(user.id, this.apartment.id!)
          .subscribe((data: ReservationDTO[]) => {
            for (const val of data) {
              this.events.push({
                id: val.id + '_1',
                title: val.apartment.title + ' Check-in',
                start: val.startDate,
                end: val.startDate,
              });
              this.events.push({
                id: val.id + '_2',
                title: val.apartment.title + ' Check-out',
                start: val.startDate,
                end: val.endDate,
              });
            }
          });
      }
    });
    this.calendarOptions = {
      plugins: [dayGridPlugin, interactionPlugin],
      initialView: 'dayGridMonth',
      events: this.events,
      selectable: true,
      editable: true,
      selectMirror: true,
      eventClick: this.handleEventClick.bind(this),
      eventMouseEnter: this.handleMouseEnter.bind(this),
      eventMouseLeave: this.handleMouseLeave.bind(this),
    };
  }

  private handleMouseLeave() {}

  private handleMouseEnter() {}

  private handleEventClick() {}
}
