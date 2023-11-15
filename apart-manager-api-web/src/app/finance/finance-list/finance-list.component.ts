import { Component } from '@angular/core';
import {EventType, Finance, Source} from "../../../generated";
import {Observable} from "rxjs";
import {Message, MessageService} from "primeng/api";
import {FormBuilder, FormGroup, Validators, FormsModule} from "@angular/forms";
import {FinanceService} from "../../finance/service/finance.service";
import {Router} from "@angular/router";

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
    { name: "PROMOTION" },
    { name: "FINE" },
    { name: "TAX" },
    { name: "CLEANING" },
    { name: "REPAIR" },
    { name: "MAINTENANCE" },
  ];


  constructor(private financeService: FinanceService,
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

  ngOnInit() {
    // this.financeService.getFinances().subscribe((data: Finance[]) => {
    //   this.finances = data;
    // });

    this.finances = [
      {
        userId: 1,
        apartmentId: 1,
        eventId: 1,
        eventType: "RENOVATION",
        source: "CLEANING",
        price: 120,
        date: new Date(),
        details: "Cleaned carpet",
      },
      {
        userId: 1,
        apartmentId: 1,
        eventId: 1,
        eventType: "RENOVATION",
        source: "REPAIR",
        price: 150,
        date: new Date('2023-10-11T03:24:00'),
        details: "Promotions",
      },
      {
        userId: 3,
        apartmentId: 2,
        eventId: 1,
        eventType: "RESERVATION",
        source: "BOOKING",
        price: 1000.79,
        date: new Date('2023-11-11T03:24:00'),
        details: "Promotions",
      },
    ];
    this.filteredFinances = [...this.finances];
    this.sumOfPrices = 0;
    this.filteredFinances.forEach((f) => {this.sumOfPrices += f.price});
  }

  // Filter criteria
  filterEventType: string | undefined;
  filterApartmentId: number | undefined;
  filterSource: string | undefined;

  applyFilter(): void {
    this.filteredFinances = this.finances.filter((finance) => {
      let eventTypeMatch = true;
      let apartmentIdMatch = true;
      let sourceMatch = true;

      if (this.filterFinanceForm.value.filterEventType.name == null) eventTypeMatch = true
      else if ("ALL" === this.filterFinanceForm.value.filterEventType.name) eventTypeMatch = true;
      else eventTypeMatch = finance.eventType === this.filterFinanceForm.value.filterEventType.name;

      if (this.filterFinanceForm.value.filterApartmentId == null) apartmentIdMatch = true;
      else if ("ALL" === this.filterFinanceForm.value.filterApartmentId) sourceMatch = true;
      else apartmentIdMatch = finance.apartmentId === this.filterFinanceForm.value.filterApartmentId;

      if (this.filterFinanceForm.value.filterSource.name == null) sourceMatch = true;
      else if ("ALL" === this.filterFinanceForm.value.filterSource.name!) sourceMatch = true;
      else sourceMatch = finance.source === this.filterFinanceForm.value.filterSource.name!;

      console.log(this.filterFinanceForm.value.filterApartmentId)
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

  deleteFinance(event: any) {
    console.log('Delete Finance:', event);
    this.messages = [{
      severity: 'info',
      detail: 'Finance deleted successfully',
    }];
    this.cleanMessages();
  }

  cleanMessages() {
    setTimeout(() => {
      this.messages = [];
    }, 3000);
  }
}
