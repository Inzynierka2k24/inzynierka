import { Component } from '@angular/core';
import 'zone.js';
import {CalendarOptions, EventInput} from '@fullcalendar/core';
import dayGridPlugin from '@fullcalendar/daygrid';
import {Message, MessageService} from "primeng/api";
import {Apartment, Reservation, UserDTO} from "../../../generated";
import {Observable} from "rxjs";
import {Store} from "@ngrx/store";
import {AppState} from "../../core/store/app.store";
import {ReservationService} from "../services/reservation.service";
import {ApartmentService} from "../../apartment/services/apartment.service";
import {Router} from "@angular/router";
import {selectCurrentUser} from "../../core/store/user/user.selectors";
import interactionPlugin from '@fullcalendar/interaction';

@Component({
  selector: 'app-reservation-calendar',
  templateUrl: './reservation-calendar.component.html',
  styleUrls: ['./reservation-calendar.component.scss']
})
export class ReservationCalendarComponent {

  messages: Message[] = [];
  reservations: Reservation[] = [];
  reservation: Reservation;
  apartments: Apartment[] = [];
  apartment: Apartment;
  user$: Observable<UserDTO | undefined>;
  // events: {id: number | undefined, title: string, start: Date; end: Date}[]= [];
  events: any[] = [];

  calendarOptions: CalendarOptions = {
    plugins: [dayGridPlugin, interactionPlugin],
    initialView: 'dayGridMonth',
    events: this.events,
    weekends: true,

    selectable: true,
    editable: true,
    selectMirror: true,
    // dateClick: this.handleDateClick.bind(this),
    dateClick: function(arg){
      alert('date click');
    }
  };

  constructor(private store: Store<AppState>,
              private reservationService: ReservationService,
              private apartmentService: ApartmentService,
              private messageService: MessageService,
              private router: Router) {
  }

  ngOnInit() {
    this.user$ = this.store.select(selectCurrentUser);
    this.user$.subscribe((user) => {
      if (user) {
        this.apartmentService.getApartments(user).subscribe((data: Apartment[]) => {
          this.apartments = data;

          if (this.apartments && this.apartments.length > 0) {
            for(const val of this.apartments) {
              this.reservationService.getReservations(user, val).subscribe((data: Reservation[]) => {
                this.reservations = this.reservations.concat(data);
              });
              this.apartment = val;
            }
            this.reservationService.getReservations(user, this.apartment).subscribe((data: Reservation[]) => {
              for(const val of this.reservations){
                  this.events.push({
                    id: val.id,
                    title: this.getApartmentTitle(val.apartmentId),
                    start: val.startDate,
                    end: val.endDate
                  });
                }

                this.calendarOptions = {
                  plugins: [dayGridPlugin],
                  initialView: 'dayGridMonth',
                  weekends: false,
                  events: this.events as EventInput[],
                };
            });
            }
        });
      }
    });
  }

  getApartmentTitle(id: number): string {
    for (const val of this.apartments){
      if (val.id == id){
        return val.title;
      }
    }
    return "";
  }

  handleDateClick(arg: any){
    // const eventReservation = this.getReservation(info.event.id);
    // this.router.navigate(['/reservations/edit', eventReservation]);
    alert('date click! ' + arg.dateStr);

  }

  getReservation(id: number): Reservation | null {
    for (const val of this.reservations) {
      if (val.id == id) {
        return val;
      }
    }
    return null;
  }
}

