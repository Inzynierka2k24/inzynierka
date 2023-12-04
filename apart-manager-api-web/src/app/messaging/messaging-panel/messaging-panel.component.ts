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
import {
  selectMessagingList,
  selectMessagingLoadingState,
} from '../../core/store/messaging/messaging.selectors';

@Component({
  selector: 'app-messaging-panel',
  templateUrl: './messaging-panel.component.html',
  styleUrls: ['./messaging-panel.component.scss'],
})
export class MessagingPanelComponent implements OnInit {
  chosenContact: ContactDTO | undefined;

  contacts$: Observable<ContactDTO[]>;
  isLoading$ = this.store.select(selectMessagingLoadingState);
  addOrderVisible = false;
  addContactVisible = false;
  contactApartments: Apartment[];
  apartmentCalendarVisible = false;
  chosenApartment: Apartment;
  editContact: any;
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

  ngOnInit(): void {}

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

  showCalendarForApartment(apartment: any) {
    this.chosenApartment = apartment;
    this.apartmentCalendarVisible = true;
  }

  deleteMessage(message: ScheduledMessageDTO) {
    if (this.currentUser.id && this.chosenContact?.id && message.id) {
      this.store.dispatch(
        MessagingActions.deleteMessage({
          userId: this.currentUser.id,
          contactId: this.chosenContact.id,
          messageId: message.id,
        }),
      );
    }
  }

  deleteContact(contact: ContactDTO) {
    if (this.currentUser.id && contact.id) {
      this.store.dispatch(
        MessagingActions.deleteContact({
          userId: this.currentUser.id,
          contactId: contact.id,
        }),
      );
    }
    this.isLoading$.subscribe((isLoading) => {
      if (!isLoading) {
        this.chosenContact = undefined;
      }
    });
  }

  openEditContactModal() {
    this.editContact = true;
    this.addContactVisible = true;
  }
}
