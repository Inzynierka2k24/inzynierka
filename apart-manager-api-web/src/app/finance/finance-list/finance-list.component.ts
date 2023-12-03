import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';
import { switchMap } from 'rxjs/operators';
import { Message, MessageService } from 'primeng/api';
import { Router } from '@angular/router';

import { FinanceService } from '../service/finance.service';
import { FinanceSourceService } from '../service/finance-source.service';
import { ApartmentService } from '../../apartment/services/apartment.service';
import { selectCurrentUser } from '../../core/store/user/user.selectors';
import { AppState } from '../../core/store/app.store';
import { Finance, Apartment, UserDTO } from '../../../generated';

@Component({
  selector: 'app-finance-list',
  templateUrl: './finance-list.component.html',
  styleUrls: ['./finance-list.component.scss']
})
export class FinanceListComponent implements OnInit {
  finances: Finance[] = [];
  filteredFinances: Finance[];
  finance: Finance;
  messages: Message[] = [];
  filterFinanceForm: FormGroup;
  sumOfPrices: number;
  sources: { name: string }[] | undefined;
  apartments: Apartment[] = [];
  apartment: Apartment;
  user: UserDTO;
  user$: Observable<UserDTO | undefined>;
  apartmentOptions: {label: string, value: number}[] = [];
  eventTypes: {name: string}[] = [];
  eventsWithSources: Map<string, string[]>;

  constructor(
    private store: Store<AppState>,
    private financeService: FinanceService,
    private apartmentService: ApartmentService,
    private messageService: MessageService,
    private financeSourceService: FinanceSourceService,
    private router: Router,
    private fb: FormBuilder
  ) {
    this.filterFinanceForm = this.fb.group({
      filterApartmentId: [null],
      filterEventType: [null],
      filterSource: [null],
    });
  }

  ngOnInit() {
    this.fetchData();
  }

  applyFilter(): void {
    this.filteredFinances = this.finances.filter(finance => {
      const eventTypeMatch = !this.filterFinanceForm.value.filterEventType || finance.eventType === this.filterFinanceForm.value.filterEventType;
      const apartmentIdMatch = !this.filterFinanceForm.value.filterApartmentId || finance.apartmentId === this.filterFinanceForm.value.filterApartmentId;
      const sourceMatch = !this.filterFinanceForm.value.filterSource || finance.source === this.filterFinanceForm.value.filterSource;

      return eventTypeMatch && apartmentIdMatch && sourceMatch;
    });
    this.sumPrices();
  }

  disableFilter(): void {
    this.filteredFinances = [...this.finances];
    this.sumPrices();
    this.filterFinanceForm.reset();
  }

  sumPrices(): void {
    this.sumOfPrices = 0;
    this.filteredFinances.forEach(f => { this.sumOfPrices += f.price });
  }

  addFinance() {
    this.router.navigate(['/finances/add']);
  }

  startEditing(finance: Finance) {
    const selectedApartment =
        this.apartmentOptions.find(ap => ap.value === finance.apartmentId);
    const financeWithApartmentLabel =
        { ...finance, apartment: selectedApartment?.label };

    this.router.navigate(['/finances/edit', financeWithApartmentLabel]);
  }

  deleteFinance(finance: Finance): void {
    this.store
    .select(selectCurrentUser)
    .pipe(
      switchMap(user => {
        if (!user) throw new Error('User not logged in');
        this.user = user;
        return this.financeService.deleteFinance(this.user, finance.id!, { responseType: 'text' });
      })
    )
    .subscribe({
      next: response => {
        this.messageService.add({ severity: 'success', summary: 'Finance deleted correctly', detail: 'success' });
        this.fetchData();
      },
      error: error => {
        console.error('API call error:', error);
        this.fetchData();
      }
    });
  }

  fetchData() {
    this.fetchFinancesForUser();
    this.fetchApartmentsForUser();
    this.fetchEventsWithSources();
  }

  fetchFinancesForUser() {
    this.user$ = this.store.select(selectCurrentUser);
    this.user$.subscribe(user => {
      if (user) {
        this.user = user;
        this.financeService.getFinances(this.user).subscribe(data => {
          this.finances = data;
          this.filteredFinances = [...this.finances];
          this.sumPrices();
        });
      }
    });
  }

  fetchApartmentsForUser() {
    this.user$ = this.store.select(selectCurrentUser);
    this.user$.subscribe(user => {
      if (user) {
        this.apartmentService.getApartments(user).subscribe(data => {
          this.apartments = data;
          this.apartmentOptions = data.map(apartment => ({
            label: `${apartment.title}, ${apartment.city}`,
            value: apartment.id!
          }));
        });
      }
    });
  }

  fetchEventsWithSources() {
    this.financeSourceService.getEventsWithSources().subscribe(data => {
      this.eventsWithSources = new Map(Object.entries(data));
      this.eventTypes = Array.from(this.eventsWithSources.keys()).map(event => ({ name: event }));
      this.eventTypes.sort((a, b) => a.name.localeCompare(b.name));

      const allSources = Array.from(this.eventsWithSources.values()).flatMap(x => x);
      const uniqueSources = Array.from(new Set(allSources));
      this.sources = uniqueSources.map(source => ({ name: source }));

    });
  }

  getApartmentTitleById(apartmentId: number): string {
    const apartment = this.apartments.find(ap => ap.id === apartmentId);
    return apartment ? `${apartment.title}, ${apartment.city}` : 'Unknown';
  }
}
