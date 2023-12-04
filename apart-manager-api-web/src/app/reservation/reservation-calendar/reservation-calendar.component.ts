import { Component, ElementRef, Renderer2 } from '@angular/core';
import 'zone.js';
import dayGridPlugin from '@fullcalendar/daygrid';
import { Message, MessageService } from 'primeng/api';
import { Apartment, Reservation, UserDTO } from '../../../generated';
import { Observable } from 'rxjs';
import { Store } from '@ngrx/store';
import { AppState } from '../../core/store/app.store';
import { ReservationService } from '../services/reservation.service';
import { ApartmentService } from '../../apartment/services/apartment.service';
import { Router } from '@angular/router';
import { selectCurrentUser } from '../../core/store/user/user.selectors';
import interactionPlugin from '@fullcalendar/interaction';

@Component({
  selector: 'app-reservation-calendar',
  templateUrl: './reservation-calendar.component.html',
  styleUrls: ['./reservation-calendar.component.scss'],
})
export class ReservationCalendarComponent {
  messages: Message[] = [];
  reservations: Reservation[] = [];
  reservation: Reservation;
  apartments: Apartment[] = [];
  apartment: Apartment;
  user$: Observable<UserDTO | undefined>;
  events: any[] = [];
  calendarOptions: any;

  constructor(
    private store: Store<AppState>,
    private reservationService: ReservationService,
    private apartmentService: ApartmentService,
    private messageService: MessageService,
    private router: Router,
    private el: ElementRef,
    private renderer: Renderer2,
  ) {}

  ngOnInit() {
    this.user$ = this.store.select(selectCurrentUser);
    this.user$.subscribe((user) => {
      if (user) {
        this.apartmentService
          .getApartments(user)
          .subscribe((data: Apartment[]) => {
            this.apartments = data;

            if (this.apartments && this.apartments.length > 0) {
              for (const val of this.apartments) {
                this.reservationService
                  .getReservations(user, val)
                  .subscribe((data: Reservation[]) => {
                    this.reservations = this.reservations.concat(data);
                  });
                this.apartment = val;
              }
              this.reservationService
                .getReservations(user, this.apartment)
                .subscribe((data: Reservation[]) => {
                  for (const val of this.reservations) {
                    this.events.push({
                      id: val.id,
                      title: this.getApartmentTitle(val.apartmentId),
                      start: val.startDate,
                      end: val.endDate,
                    });
                  }
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
    this.createTooltipOnEnter();
  }

  getApartmentTitle(id: number): string {
    for (const val of this.apartments) {
      if (val.id == id) {
        return val.title;
      }
    }
    return '';
  }

  handleEventClick(arg: any) {
    const eventReservation = this.getReservation(arg.event.id);
    this.router.navigate(['/reservations/edit', eventReservation]);
  }

  getReservation(id: number): Reservation | null {
    for (const val of this.reservations) {
      if (val.id == id) {
        return val;
      }
    }
    return null;
  }

  createTooltipOnEnter(): void {
    const htmlCode = `
      <div class="custom-tooltip">
        <b>Title:</b><br/>
        Start: <br/>
        End:
      </div>
    `;
    const container = document.getElementById('tooltip-container');
    this.renderer.appendChild(container, this.createHtmlElement(htmlCode));
  }

  handleMouseEnter(info: any): void {
    const htmlCode = `
      <div class="custom-tooltip">
        <b>${info.event.title}</b><br/>
        Start: ${info.event.start.toLocaleString()}<br/>
        End: ${info.event.end.toLocaleString()}
      </div>
    `;
    const container = document.getElementById('tooltip-container');
    if (container) {
      container.innerHTML = '';
      this.renderer.appendChild(container, this.createHtmlElement(htmlCode));
    }
  }

  handleMouseLeave(info: any): void {
    const container = document.getElementById('tooltip-container');
    if (container) {
      const tooltip = container.lastChild;
      if (tooltip) {
        this.renderer.removeChild(container, tooltip);
      }
    }
  }

  private createHtmlElement(htmlCode: string): HTMLElement {
    const div = this.renderer.createElement('div');
    this.renderer.setProperty(div, 'innerHTML', htmlCode);
    return div;
  }
}
