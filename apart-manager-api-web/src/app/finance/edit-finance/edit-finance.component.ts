import { Component } from '@angular/core';
import {Message, MessageService} from "primeng/api";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {FinanceService} from "../../finance/service/finance.service";
import {ActivatedRoute, Router} from "@angular/router";
import {EventType, Finance, Source} from "../../../generated";

@Component({
  selector: 'app-edit-finance',
  templateUrl: './edit-finance.component.html',
  styleUrls: ['../add-finance/add-finance.component.scss']
})
export class EditFinanceComponent {
  finance: any;
  messages: Message[] = [];
  editFinanceForm: FormGroup;
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

  constructor(private financeService: FinanceService,
              private messageService: MessageService,
              private router: Router,
              private fb: FormBuilder,
              private route: ActivatedRoute) {

    this.route.params.subscribe((params) => {
      this.finance = params;
    });

    this.editFinanceForm = this.fb.group({
        userId: ['', Validators.required],
        apartmentId: ['', Validators.required],
        eventId: ['', Validators.required],
        eventType: ['', [Validators.required]],
        source: ['', [Validators.required]],
        price: ['', [Validators.required, Validators.min(0)]],
        date: ['', [Validators.required]],
        details: ['', [Validators.required]],
    });

    this.editFinanceForm.setValue({
        userId: this.editFinanceForm.value.userId!,
        apartmentId: this.editFinanceForm.value.apartmentId!,
        eventId: this.editFinanceForm.value.eventId!,
        eventType: this.editFinanceForm.value.eventType! as EventType,
        source: this.editFinanceForm.value.source! as Source,
        price: this.editFinanceForm.value.price!,
        date: this.editFinanceForm.value.date!,
        details: this.editFinanceForm.value.details!,
    });
  }

  addFinance() {
    this.router.navigate(['/finances/add']);
  }

  saveChanges(finance: Finance) {
    console.log('Edit Finance:', event);
    this.messages = [{
      severity: 'success',
      detail: 'Finance edited successfully',
    }];
    this.cleanMessages();
    // this.financeService.updateFinance(this.finance).subscribe((response) => {
    //   this.isEditing = false;
    // }, (error) => {
    //   this.messages = [{
    //     severity: 'warn',
    //     detail: 'Cannot update finance',
    //   }];
    // });
    this.router.navigate(['/finances']);

  }

  cancelEditing() {
    this.router.navigate(['/finances']);
  }

  cleanMessages() {
    setTimeout(() => {
      this.messages = [];
    }, 3000);
  }
}
