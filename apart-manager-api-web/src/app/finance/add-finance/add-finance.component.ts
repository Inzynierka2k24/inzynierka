import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Store} from "@ngrx/store";
import {Finance, Apartment, UserDTO} from "../../../generated";
import {AppState} from "../../core/store/app.store";
import {FinanceService} from "../service/finance.service";
import {selectCurrentUser} from "../../core/store/user/user.selectors";
import {MessageService} from "primeng/api";
import {switchMap} from "rxjs/operators";
import {FinanceSourceService} from "../service/finance-source.service";
import {Observable} from "rxjs";
import {ApartmentService} from "../../apartment/services/apartment.service";

@Component({
  selector: 'app-add-finance',
  templateUrl: './add-finance.component.html',
  styleUrls: ['./add-finance.component.scss']
})
export class AddFinanceComponent implements OnInit {
  addFinanceForm;
  eventsWithSources: Map<string, string[]>;
  selectedEvent: string;
  eventKeys: string[] = [];
  eventSources: string[] = [];
  isUserLoggedIn = false;
  user$: Observable<UserDTO | undefined>;
  user: UserDTO;
  apartments: {label: string, value: Apartment}[];
  apartment: Apartment;


  constructor(private store: Store<AppState>,
              private apartmentService: ApartmentService,
              private financeService: FinanceService,
              private financeSourceService: FinanceSourceService,
              private formBuilder: FormBuilder,
              private messageService: MessageService){
    this.addFinanceForm = formBuilder.nonNullable.group(
      {
        apartment: ['', Validators.required],
        eventType: ['', [Validators.required]],
        source: ['', [Validators.required]],
        price: ['', [Validators.required]],
        date: ['', [Validators.required]],
        details: ['', [Validators.required]],
      })
  }

  ngOnInit(): void {
    this.fetchEventsWithSources();
    this.fetchApartmentsForUser();
  }

  fetchEventsWithSources() {
    this.financeSourceService.getEventsWithSources().subscribe((data: Map<string, string[]>) => {
      this.eventsWithSources = new Map(Object.entries(data));
      this.eventKeys = Array.from(this.eventsWithSources.keys());
    });
  }

  fetchApartmentsForUser(){
    this.user$ = this.store.select(selectCurrentUser);
    this.user$.subscribe((user) => {
      if (user) {
        this.apartmentService.getApartments(user).subscribe((data: Apartment[]) => {
          this.apartments = data.map(apartment => ({
            label: `${apartment.title}, ${apartment.city}`,
            value: apartment
          }));
        });
      }
    });
  }

  onEventSelect() {
    if (this.selectedEvent) {
      const sources = this.eventsWithSources.get(this.selectedEvent) || [];
      this.eventSources = sources;
    } else {
      this.eventSources = [];
    }
  }

  addFinance(): void {
    if (this.addFinanceForm.valid) {
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
              userId: this.user.id,
              apartmentId: parseInt(this.addFinanceForm.value.apartment!),
              eventType: this.addFinanceForm.value.eventType!,
              source: this.addFinanceForm.value.source!,
              price: parseFloat(this.addFinanceForm.value.price!),
              date: new Date(this.addFinanceForm.value.date!),
              details: this.addFinanceForm.value.details!,
            };
            return this.financeService.addFinance(this.user,
              financeData,
              { responseType: 'text' });
          })
        )
        .subscribe(
          {
          next: response =>{
            this.addFinanceForm.reset();
            this.messageService.add({
              severity: 'success',
              summary: 'Finance added correctly',
              detail: 'success',
            })},
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
      this.markAllFieldsAsTouched(this.addFinanceForm);
    }
  }

  private markAllFieldsAsTouched(formGroup: FormGroup): void {
    Object.keys(formGroup.controls).forEach((controlName) => {
      const control = formGroup.get(controlName);
      if (control instanceof FormGroup) {
        this.markAllFieldsAsTouched(<FormGroup<any>>control);
      } else {
        control?.markAsTouched();
      }
    });
  }

  getPriceTooltip(): string {
    const eventType = this.addFinanceForm.value.eventType;
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
}
