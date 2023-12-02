import { Component, OnInit } from '@angular/core';
import { first, map, Observable } from 'rxjs';
import { AppState } from '../../core/store/app.store';
import { Store } from '@ngrx/store';
import {
  Apartment,
  ContactDTO,
  ContactType,
  ScheduledMessageDTO,
  UserDTO,
} from '../../../generated';
import MessagingActions from '../../core/store/messaging/messaging.actions';
import { userStateSelector } from '../../core/store/user/user.selectors';
import { selectMessagingList } from '../../core/store/messaging/messaging.selectors';

@Component({
  selector: 'app-messaging-panel',
  templateUrl: './messaging-panel.component.html',
  styleUrls: ['./messaging-panel.component.scss'],
})
export class MessagingPanelComponent implements OnInit {
  chosenContact: ContactDTO;

  contacts$: Observable<ContactDTO[]>;
  addOrderVisible = false;
  addContactVisible = false;
  contactApartments: Apartment[];
  private currentUser: UserDTO;

  constructor(private store: Store<AppState>) {
    this.store
      .select(userStateSelector)
      .pipe(first((state) => !!state?.user))
      .subscribe((state) => {
        if (state?.user?.id) {
          this.currentUser = state.user;
          this.store.dispatch(
            MessagingActions.loadContacts({ userId: state.user.id }),
          );
        }
      });
    this.contacts$ = this.store
      .select(selectMessagingList)
      .pipe(map((contacts) => contacts || []));
  }

  ngOnInit(): void {
    // this.reservationService
    //   .getReservationDTOs(this.currentUser.id, this.apartment)
    //   .pipe(
    //     map((reservations) =>
    //       reservations.map((reservation) => ({
    //         id: reservation.id,
    //         title: reservation.apartment.title,
    //         start: reservation.startDate,
    //         end: reservation.endDate,
    //       })),
    //     ),
    //   )
    //   .subscribe((data: Reservation[]) => {
    //     for (const val of this.reservations) {
    //       this.events.push({
    //         id: val.id,
    //         title: this.getApartmentTitle(val.apartmentId),
    //         start: val.startDate,
    //         end: val.endDate,
    //       });
    //     }
    //   });
    // this.calendarOptions = {
    //   plugins: [dayGridPlugin, interactionPlugin],
    //   initialView: 'dayGridMonth',
    //   events: this.events,
    //   selectable: true,
    //   editable: true,
    //   selectMirror: true,
    //   eventClick: this.handleEventClick.bind(this),
    //   eventMouseEnter: this.handleMouseEnter.bind(this),
    //   eventMouseLeave: this.handleMouseLeave.bind(this),
    // };
  }

  getIcon(ct: ContactType) {
    switch (ct) {
      case 'CLEANING':
        return 'pi pi-trash';
      case 'ELECTRICIAN':
      case 'MECHANIC':
        return 'pi pi-cog';
      default:
        return 'pi pi-dollar';
    }
  }

  chooseContact(contact: ContactDTO) {
    this.chosenContact = contact;

    this.contactApartments = contact.messages.map((m) => m.apartments).flat();
    const map = new Map(this.contactApartments.map((a) => [a.id, a]));
    this.contactApartments = Array.from(map.values());
  }

  apartmentStreetToString(a: Apartment) {
    return (
      a.city +
      ', ' +
      a.street +
      ' ' +
      a.buildingNumber +
      '/' +
      a.apartmentNumber
    );
  }

  apartmentsListForMessage(message: ScheduledMessageDTO) {
    return message.apartments.map((a) => a.title).join(', ');
  }
}
