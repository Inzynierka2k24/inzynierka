import { Component, OnInit } from '@angular/core';
import { ApartmentService } from '../../core/services/apartment/apartment.service';
import { Apartment } from '../../../generated';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';
import { selectCountApartments } from '../../core/store/apartment/apartment.selector';

@Component({
  selector: 'app-apartment-list',
  templateUrl: './apartment-list.component.html',
  styleUrls: ['./apartment-list.component.scss'],
})
export class ApartmentListComponent implements OnInit {
  apartments: Apartment[] = [];
  countApartments$: Observable<number>;

  constructor(
    private apartmentService: ApartmentService,
    private store: Store,
  ) {
    this.countApartments$ = store.select(selectCountApartments);
  }

  ngOnInit() {
    this.apartmentService.getApartments().subscribe((data) => {
      this.apartments = data;
    });
  }
}
