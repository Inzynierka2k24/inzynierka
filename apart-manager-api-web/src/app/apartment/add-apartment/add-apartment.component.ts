import { Component } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Store} from "@ngrx/store";
import ApartmentActions from "../store/apartment.actions";
import {MessageService} from "primeng/api";
import {AppState} from "../../core/store/app.store";
import {selectCurrentUser} from "../../core/store/user/user.selectors";
import {Apartment, UserDTO} from "../../../generated";
import {ApartmentService} from "../services/apartment.service";

@Component({
  selector: 'app-add-apartment',
  templateUrl: './add-apartment.component.html',
  styleUrls: ['./add-apartment.component.scss']
})
export class AddApartmentComponent {

  isUserLoggedIn = false;
  addApartForm;
  user: UserDTO;

  constructor(private formBuilder: FormBuilder,
              private store: Store<AppState>,
              private apartmentService: ApartmentService,
              private messageService: MessageService){

    this.addApartForm = formBuilder.nonNullable.group(
      {
        dailyPrice: ['', [Validators.required, Validators.min(0)]],
        title: ['', [Validators.required]],
        country: ['', [Validators.required]],
        city: ['', [Validators.required]],
        street: ['', [Validators.required]],
        buildingNumber: ['', [Validators.required, Validators.min(0)]],
        apartmentNumber: ['', [Validators.required, Validators.min(0)]],
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
          buildingNumber: this.addApartForm.value.buildingNumber!,
          apartmentNumber: this.addApartForm.value.apartmentNumber!,
        }),
      )
      this.addApartForm.reset();

      const apartmentData: Apartment = {
          dailyPrice: parseInt(this.addApartForm.value.dailyPrice!),
          title: this.addApartForm.value.title!,
          country: this.addApartForm.value.country!,
          city: this.addApartForm.value.city!,
          street: this.addApartForm.value.street!,
          buildingNumber: this.addApartForm.value.buildingNumber!,
          apartmentNumber: this.addApartForm.value.apartmentNumber!,
      };

      const apartmentDataSub = this.store
        .select(selectCurrentUser)
        .subscribe((user) => {
          if (user) {
            this.isUserLoggedIn = true;
            this.user = user;
            this.apartmentService.addApartment(this.user, apartmentData).subscribe(
              (result: string) => {
                  this.addApartForm.reset()

            },
        (error) => {
                console.error('API call error:', error); //TODO
              }
            );
          } else {
            this.isUserLoggedIn = false;
          }
        });
    }
    else {
        this.messageService.add({
          severity: 'error',
          summary: 'Validation Error',
          detail: 'Please fill in all required fields and correct validation errors.',
        });
        this.markAllFieldsAsTouched(this.addApartForm);
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
