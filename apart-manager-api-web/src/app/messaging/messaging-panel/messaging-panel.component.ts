import { Component, OnInit } from '@angular/core';
import { Observable, of } from 'rxjs';
import { AppState } from '../../core/store/app.store';
import { Store } from '@ngrx/store';
import { MessagingService } from '../messaging.service';
import { ContactDTO, ContactType } from '../../../generated';
import { apartmentStreetToString, MockFactory } from '../../core/utils';

@Component({
  selector: 'app-messaging-panel',
  templateUrl: './messaging-panel.component.html',
  styleUrls: ['./messaging-panel.component.scss'],
})
export class MessagingPanelComponent implements OnInit {
  chosenContact: ContactDTO;

  apartment1 = MockFactory.createMockApartment({});
  apartment2 = MockFactory.createMockApartment({});
  contact1 = MockFactory.createMockContact({
    apartments: [this.apartment1, this.apartment2],
    name: 'Cleaner',
    contactType: 'CLEANING',
  });

  services$ = of([
    this.contact1,
    {
      name: 'zbyszek',
      type: 'Zbyszek',
      status: 'Tak',
    },
    {
      name: 'zbyszek',
      type: 'Zbyszek',
      status: 'Tak',
    },
    {
      name: 'zbyszek',
      type: 'Zbyszek',
      status: 'Tak',
    },
  ]) as Observable<any>;
  protected readonly apartmentStreetToString = apartmentStreetToString;

  constructor(
    private store: Store<AppState>,
    private messagingService: MessagingService,
  ) {}

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
}
