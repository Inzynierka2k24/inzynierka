import { Component, OnInit } from '@angular/core';
import { first, map, Observable } from 'rxjs';
import { AppState } from '../../core/store/app.store';
import { Store } from '@ngrx/store';
import { Apartment, ContactDTO, ContactType } from '../../../generated';
import { apartmentStreetToString } from '../../core/utils';
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
  protected readonly apartmentStreetToString = apartmentStreetToString;

  constructor(private store: Store<AppState>) {
    this.store
      .select(userStateSelector)
      .pipe(first((state) => !!state?.user))
      .subscribe((state) => {
        if (state?.user?.id)
          this.store.dispatch(
            MessagingActions.loadContacts({ userId: state.user?.id }),
          );
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
  }
}
