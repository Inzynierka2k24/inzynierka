import { Component } from '@angular/core';
import {Apartment} from "../../../generated";
import {Message, MessageService} from "primeng/api";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {ApartmentService} from "../services/apartment.service";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-edit-apartment',
  templateUrl: './edit-apartment.component.html',
  styleUrls: ['./edit-apartment.component.scss']
})
export class EditApartmentComponent {
  apartment: any;
  messages: Message[] = [];
  editApartmentForm: FormGroup;


  constructor(private apartmentService: ApartmentService,
              private messageService: MessageService,
              private router: Router,
              private fb: FormBuilder,
              private route: ActivatedRoute) {

    this.route.params.subscribe((params) => {
      this.apartment = params;
    });

    this.editApartmentForm = this.fb.group({
      dailyPrice: [null, Validators.required],
      title: ['', Validators.required],
      country: ['', Validators.required],
      city: ['', Validators.required],
      street: ['', Validators.required],
      buildingNumber: ['', Validators.required],
      apartmentNumber: ['', Validators.required],
    });

    this.editApartmentForm.setValue({
      dailyPrice: this.apartment.dailyPrice,
      title: this.apartment.title,
      country: this.apartment.country,
      city: this.apartment.city,
      street: this.apartment.street,
      buildingNumber: this.apartment.buildingNumber,
      apartmentNumber: this.apartment.apartmentNumber
    });
  }

  addApartment() {
    this.router.navigate(['/apartments/add']);
  }

  saveChanges(apartment: Apartment) {
    console.log('Edit Apartment:', event);
    this.messages = [{
      severity: 'success',
      detail: 'Apartment edited successfully',
    }];
    this.cleanMessages();
    // this.apartmentService.updateApartment(this.apartment).subscribe((response) => {
    //   this.isEditing = false;
    // }, (error) => {
    //   this.messages = [{
    //     severity: 'warn',
    //     detail: 'Cannot update apartment',
    //   }];
    // });
    this.router.navigate(['/apartments']);

  }

  cancelEditing() {
    this.router.navigate(['/apartments']);
  }

  cleanMessages() {
    setTimeout(() => {
      this.messages = [];
    }, 3000);
  }
}
