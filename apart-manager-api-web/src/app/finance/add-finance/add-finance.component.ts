import { Component } from '@angular/core';
import {FormBuilder, Validators} from "@angular/forms";
import {Store} from "@ngrx/store";
import FinanceActions from "../store/finance.actions";
import {Finance, Source, EventType} from "../../../generated";

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
  constructor(private formBuilder: FormBuilder, private store: Store){

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

      // FinanceActions.addFinance(financeData)
      //   .subscribe((result: boolean) => {
      //     if (result) {
      //       // TODO add message
      //       this.addFinanceForm.reset();
      //     } else {
      //       // TODO Handle API call error
      //     }
      //   });
    } else {
      // TODO Handle validation errors
    }
  }

}
