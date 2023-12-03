import {Component} from '@angular/core';
import {Apartment, Finance, UserDTO} from "../../../generated";
import {Observable} from "rxjs";
import {Message, MessageService} from "primeng/api";
import {FormBuilder, FormGroup} from "@angular/forms";
import {FinanceService} from "../service/finance.service";
import {Router} from "@angular/router";
import {Store} from "@ngrx/store";

import {selectCurrentUser} from "../../core/store/user/user.selectors";
import {AppState} from "../../core/store/app.store";
import {ApartmentService} from "../../apartment/services/apartment.service";
import {switchMap} from "rxjs/operators";

interface WeeklyRevenue {
  weekStartDate: Date;
  totalRevenue: number;
}

@Component({
  selector: 'app-finance-list',
  templateUrl: './finance-list.component.html',
  styleUrls: ['./finance-list.component.scss']
})
export class FinanceListComponent {
  finances: Finance[] = [];
  filteredFinances: Finance[];
  finance: Finance;
  messages: Message[] = [];
  isEditing = true;
  filterFinanceForm: FormGroup;
  weeklyRevenueData: WeeklyRevenue[] = [];
  sumOfPrices: number;
  eventTypes = [
    { name: "ALL" },
    { name: "UNKNOWN" },
    { name: "RESERVATION" },
    { name: "RENOVATION" },
  ];
  sources = [
    { name: "ALL" },
    { name: "UNKNOWN" },
    { name: "BOOKING" },
    { name: "AIRBNB" },
    { name: "TRIVAGO" },
    { name: "NOCOWANIEPL" },
    { name: "PROMOTION" },
    { name: "FINE" },
    { name: "TAX" },
    { name: "CLEANING" },
    { name: "REPAIR" },
    { name: "MAINTENANCE" },
  ];
  isUserLoggedIn = false;
  apartments: Apartment[] = [];
  apartment: Apartment;
  user: UserDTO;
  user$: Observable<UserDTO | undefined>;



  constructor(private store: Store<AppState>,
              private financeService: FinanceService,
              private apartmentService: ApartmentService,
              private messageService: MessageService,
              private router: Router,
              private fb: FormBuilder)
  {
    this.filterFinanceForm = this.fb.group({
      filterApartmentId: [null],
      filterEventType: [null],
      filterSource: [null],
    });

    this.filterFinanceForm.setValue({
      filterApartmentId: "ALL",
      filterEventType: "ALL",
      filterSource: "ALL",
    });
  }

  ngAfterViewInit() {
    this.fetchData();
  }

  // Filter criteria
  filterEventType: string | undefined;
  filterApartmentId: number | undefined;
  filterSource: string | undefined;

  applyFilter(): void {
    this.filteredFinances = this.finances.filter((finance) => {
      let eventTypeMatch: boolean;
      let apartmentIdMatch = true;
      let sourceMatch: boolean;

      if (this.filterFinanceForm.value.filterEventType.name == null) eventTypeMatch = true
      else if ("ALL" === this.filterFinanceForm.value.filterEventType.name) eventTypeMatch = true;
      else eventTypeMatch = finance.eventType === this.filterFinanceForm.value.filterEventType.name;

      if (this.filterFinanceForm.value.filterApartmentId == null) apartmentIdMatch = true;
      else if ("ALL" === this.filterFinanceForm.value.filterApartmentId) sourceMatch = true;
      else apartmentIdMatch = finance.apartmentId === this.filterFinanceForm.value.filterApartmentId;

      if (this.filterFinanceForm.value.filterSource.name == null) sourceMatch = true;
      else if ("ALL" === this.filterFinanceForm.value.filterSource.name!) sourceMatch = true;
      else sourceMatch = finance.source === this.filterFinanceForm.value.filterSource.name!;

      return eventTypeMatch && apartmentIdMatch && sourceMatch;
    });
    this.sumPrices();
  }

  disableFilter(): void {
    this.filteredFinances = [...this.finances];
    this.sumPrices();
  }

  sumPrices(): void {
    this.sumOfPrices = 0;

    //TODO renovation is outcome reservation is income
    this.filteredFinances.forEach((f) => {this.sumOfPrices += f.price});
  }

  addFinance() {
    this.router.navigate(['/finances/add']);
  }

  startEditing(finance: Finance) {
    this.router.navigate(['/finances/edit', finance]);
  }

  deleteFinance(finance: Finance): void {
    this.store
      .select(selectCurrentUser)
      .pipe(
        switchMap((user) => {
          if (!user) {
            throw new Error('User not logged in');
          }
          this.user = user;
          return this.financeService.deleteFinance(this.user, <number>finance.id, { responseType: 'text' });
        })
      )
      .subscribe(
        {
        next: response =>{
          this.messageService.add({
            severity: 'success',
            summary: 'Finance deleted correctly',
            detail: 'success',
          });
          this.fetchData();
        },
        error:error => {
            console.error('API call error:', error);
            this.fetchData();
          },
        },
      );
  }

  cleanMessages() {
    setTimeout(() => {
      this.messages = [];
    }, 3000);
  }

  fetchData() {
    this.user$ = this.store.select(selectCurrentUser);
    this.user$.subscribe((user) => {
      if (user) {
        this.user = user;
        this.financeService.getFinances(this.user).subscribe((data: Finance[]) => {
          this.finances = data;
          this.filteredFinances = [...this.finances];
          this.sumOfPrices = 0;
          this.filteredFinances.forEach((f) => { this.sumOfPrices += f.price });
        });
      }
    });
  }
}
