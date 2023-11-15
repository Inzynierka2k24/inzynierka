import { Component } from '@angular/core';
import {FormBuilder, Validators} from "@angular/forms";
import {Store} from "@ngrx/store";
import ApartmentActions from "../store/apartment.actions";

@Component({
  selector: 'app-add-apartment',
  templateUrl: './add-apartment.component.html',
  styleUrls: ['./add-apartment.component.scss']
})
export class AddApartmentComponent {

  addApartForm;

  constructor(private formBuilder: FormBuilder, private store: Store){
    this.addApartForm = formBuilder.nonNullable.group(
      {
        dailyPrice: ['', Validators.required, Validators.min(0)],
        title: ['', [Validators.required]],
        country: ['', Validators.required],
        city: ['', [Validators.required]],
        street: ['', Validators.required],
        bulidingNumber: ['', Validators.required, Validators.min(0)],
        apartmentNumber: ['', Validators.required, Validators.min(0)],
      })
  }

  addApartment(): void {
    if (this.addApartForm.valid){
      this.store.dispatch(
        ApartmentActions.addApartment({
          dailyPrice: parseInt(this.addApartForm.value.dailyPrice!),
          title: this.addApartForm.value.title!,
          country: this.addApartForm.value.country!,
          city: this.addApartForm.value.city!,
          street: this.addApartForm.value.street!,
          buildingNumber: this.addApartForm.value.bulidingNumber!,
          apartmentNumber: this.addApartForm.value.apartmentNumber!,
        }),
      )
      this.addApartForm.reset();
    }
    else {
      //TODO validation errors
    }
  }

}
