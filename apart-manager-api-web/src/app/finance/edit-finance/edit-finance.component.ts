import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Store} from "@ngrx/store";
import {Apartment, Finance, UserDTO} from "../../../generated";
import {AppState} from "../../core/store/app.store";
import {FinanceService} from "../service/finance.service";
import {selectCurrentUser} from "../../core/store/user/user.selectors";
import {Message, MessageService} from "primeng/api";
import {FinanceSourceService} from "../service/finance-source.service";
import {Observable} from "rxjs";
import {ApartmentService} from "../../apartment/services/apartment.service";
import {ActivatedRoute, Router} from "@angular/router";
import defaultCallbacks from "chart.js/dist/plugins/plugin.tooltip";
import {switchMap} from "rxjs/operators";

@Component({
  selector: 'app-edit-finance',
  templateUrl: './edit-finance.component.html',
  styleUrls: ['./edit-finance.component.scss']
})
export class EditFinanceComponent implements OnInit {
  messages: Message[] = [];
  editFinanceForm: FormGroup;
  finance: any;
  eventsWithSources: Map<string, string[]>;
  eventKeys: string[] = [];
  eventSources: string[] = [];
  isUserLoggedIn = false;
  user$: Observable<UserDTO | undefined>;
  user: UserDTO;
  userApartments: Apartment[]= [];
  apartments: {label: string, value: number}[];

  constructor(private store: Store<AppState>,
              private apartmentService: ApartmentService,
              private financeService: FinanceService,
              private financeSourceService: FinanceSourceService,
              private messageService: MessageService,
              private router: Router,
              private fb: FormBuilder,
              private route: ActivatedRoute){
    this.route.params.subscribe((params) => {
      this.finance = params;
      console.log(this.finance)

      // to ensure that data for Source and Apartment dropdowns is not empty
      this.eventSources = [this.finance.source!];
      this.apartments = [{label: this.finance.apartment!, value: this.finance.apartmentId!}]
    });

    this.editFinanceForm = this.fb.group({
      userId: ['', Validators.required],
      apartment: ['', Validators.required],
      eventType: ['', [Validators.required]],
      source: ['', [Validators.required]],
      price: ['', [Validators.required]],
      date: ['', [Validators.required]],
      details: ['', [Validators.required]],
    });

    this.editFinanceForm.setValue({
      userId: this.finance.userId!,
      apartment: this.finance.apartment,
      eventType: this.finance.eventType!,
      source: this.finance.source!,
      price: this.finance.price!,
      date: this.convertDate(this.finance.date!),
      details: this.finance.details!,
    });
  }

  ngOnInit(): void {
    this.fetchEventsWithSources();
    this.fetchApartmentsForUser();
  }

  convertDate(date: string) {
    return new Date(date);
  }

  fetchEventsWithSources() {
    this.financeSourceService.getEventsWithSources().subscribe((data: Map<string, string[]>) => {
      this.eventsWithSources = new Map(Object.entries(data));
      this.eventSources = this.eventsWithSources.get(this.finance.eventType)!;
      this.eventKeys = Array.from(this.eventsWithSources.keys());
    });
  }

  fetchApartmentsForUser(){
    this.user$ = this.store.select(selectCurrentUser);
    this.user$.subscribe((user) => {
      if (user) {
        this.apartmentService.getApartments(user).subscribe((data: Apartment[]) => {
          this.userApartments = data;
          this.apartments = data.map(apartment => ({
            label: `${apartment.title}, ${apartment.city}`,
            value: apartment.id!
          }));
        });
      }
    });
  }

  onEventSelect() {
    this.eventSources = this.eventsWithSources.get(this.editFinanceForm.value.eventType!) || [];
  }

  getPriceTooltip(): string {
    const eventType = this.editFinanceForm.value.eventType;
    const message =
        "Enter the price as a positive value for incomes " +
        "or negative value for losses. "

    if (eventType === 'RESERVATION') {
      return message + "Generally Reservations shall be incomes."
    } else if (eventType === 'RENOVATION') {
      return message + "Generally Renovations shall be losses."
    } else {
      return message;
    }
  }

  updateFinance(): void {
    if (this.editFinanceForm.valid) {
      this.store
      .select(selectCurrentUser)
      .pipe(
        switchMap((user) => {
          if (!user) {
            this.isUserLoggedIn = false;
            throw new Error('User not logged in');
          }
          this.isUserLoggedIn = true;
          this.user = user;
          const financeData: Finance = {
            id: this.finance.id!,
            userId: this.user.id,
            apartmentId: parseInt(this.editFinanceForm.value.apartment!),
            eventType: this.editFinanceForm.value.eventType!,
            source: this.editFinanceForm.value.source!,
            price: parseFloat(this.editFinanceForm.value.price!),
            date: new Date(this.editFinanceForm.value.date!),
            details: this.editFinanceForm.value.details!,
          };
          return this.financeService.updateFinance(this.user,
            financeData,
              { responseType: 'text' });
        })
      )
      .subscribe(
        {
          next: response =>{
            this.editFinanceForm.reset();
            this.messageService.add({
              severity: 'success',
              summary: 'Finance updated correctly',
              detail: 'success',
            })

            this.router.navigate(['/finances']); // Replace 'your-target-route' with the desired route
          },
          error:error => {
            console.error('API call error:', error);
          }
        },
      );
    } else {
      this.messageService.add({
        severity: 'error',
        summary: 'Validation Error',
        detail: 'Please fill in all required fields and correct validation errors.',
      });
    }
  }
}
