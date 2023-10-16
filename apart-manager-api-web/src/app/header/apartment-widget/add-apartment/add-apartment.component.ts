import { Component, ViewChild } from '@angular/core';
import { InputText } from 'primeng/inputtext';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Apartment} from "../../../../generated";
import {FormBuilder, Validators} from "@angular/forms";
import {Store} from "@ngrx/store";
import ApartmentActions, {addApartment} from "../../../core/store/apartment/apartment.actions";
import {ApartmentActionTypes} from "../../../core/store/apartment/apartment.store";

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




  // @ViewChild('dailyPrice') dailyPriceInput: InputText;
  // @ViewChild('title') titleInput: InputText;
  // @ViewChild('country') countryInput: InputText;
  // @ViewChild('city') cityInput: InputText;
  // @ViewChild('street') streetInput: InputText;
  // @ViewChild('buildingNumber') buildingNumberInput: InputText;
  // @ViewChild('apartmentNumber') apartmentNumberInput: InputText;

  // addApartment(): void {
    // const apartmentData = {
    //   dailyPrice: this.dailyPriceInput.nativeElement.value,
    //   title: this.titleInput.nativeElement.value,
    //   country: this.countryInput.nativeElement.value,
    //   city: this.cityInput.nativeElement.value,
    //   street: this.streetInput.nativeElement.value,
    //   buildingNumber: this.buildingNumberInput.nativeElement.value,
    //   apartmentNumber: this.apartmentNumberInput.nativeElement.value
    // };
    //
    // // Here, you can add the apartment data to your data source
    // console.log(apartmentData); // For demonstration, log the data
  // }

}
