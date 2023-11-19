import { Component } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {resultMemoize, Store} from "@ngrx/store";
import {Finance, Source, EventType, Apartment, UserDTO} from "../../../generated";
import {AppState} from "../../core/store/app.store";
import {FinanceService} from "../service/finance.service";
import {selectCurrentUser} from "../../core/store/user/user.selectors";
import {MessageService} from "primeng/api";

@Component({
  selector: 'app-add-finance',
  templateUrl: './add-finance.component.html',
  styleUrls: ['./add-finance.component.scss']
})
export class AddFinanceComponent {
  addFinanceForm;
  eventTypes = [
    { name: "UNKNOWN" },
    { name: "RESERVATION" },
    { name: "RENOVATION" },
  ];
  sources = [
    { name: "UNKNOWN" },
    { name: "BOOKING" },
    { name: "PROMOTION" },
    { name: "FINE" },
    { name: "TAX" },
    { name: "CLEANING" },
    { name: "REPAIR" },
    { name: "MAINTENANCE" },
  ];
  isUserLoggedIn = false;
  user: UserDTO;

  constructor(private store: Store<AppState>,
              private financeService: FinanceService,
              private formBuilder: FormBuilder,
              private messageService: MessageService){
    this.addFinanceForm = formBuilder.nonNullable.group(
      {
        userId: ['', Validators.required],
        apartmentId: ['', Validators.required],
        eventId: ['', Validators.required],
        eventType: ['', [Validators.required]],
        source: ['', [Validators.required]],
        price: ['', [Validators.required, Validators.min(0)]],
        date: ['', [Validators.required]],
        details: ['', [Validators.required]],
      })
  }

  addFinance(): void {
    if (this.addFinanceForm.valid) {
      const financeData: Finance = {
        userId: parseInt(this.addFinanceForm.value.userId!),
        apartmentId: parseInt(this.addFinanceForm.value.apartmentId!),
        eventId: parseInt(this.addFinanceForm.value.eventId!),
        eventType: this.addFinanceForm.value.eventType! as EventType,
        source: this.addFinanceForm.value.source! as Source,
        price: parseFloat(this.addFinanceForm.value.price!),
        date: new Date(this.addFinanceForm.value.date!),
        details: this.addFinanceForm.value.details!,
      };
      // const financeData: Finance = {
      //   userId: parseInt(this.addFinanceForm.value.userId!),
      //   apartmentId: parseInt(this.addFinanceForm.value.apartmentId!),
      //   eventId: parseInt(this.addFinanceForm.value.eventId!),
      //   eventType: EventType[this.addFinanceForm.value.eventType],
      //   source: Source[this.addFinanceForm.value.source],
      //   price: parseFloat(this.addFinanceForm.value.price!),
      //   date: new Date(this.addFinanceForm.value.date!),
      //   details: this.addFinanceForm.value.details!,
      // };
      console.log(financeData);
      const financeDataSub = this.store
        .select(selectCurrentUser)
        .subscribe((user) => {
          if (user) {
            this.isUserLoggedIn = true;
            this.user = user;
            this.financeService.addFinance(this.user, financeData).subscribe(
              (result: boolean) => {
                if (result){
                  this.addFinanceForm.reset()
                } else {
                  // TODO Handle API call error
                }
            },
        (error) => {
                console.error('API call error:', error); //TODO
              }
            );
          } else {
            this.isUserLoggedIn = false;
          }
        });
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
}
