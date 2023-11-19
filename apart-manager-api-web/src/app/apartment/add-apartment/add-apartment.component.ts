import { Component } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Store} from "@ngrx/store";
import {MessageService} from "primeng/api";
import {AppState} from "../../core/store/app.store";
import {selectCurrentUser} from "../../core/store/user/user.selectors";
import {Apartment, UserDTO} from "../../../generated";
import {ApartmentService} from "../services/apartment.service";
import { switchMap } from 'rxjs/operators';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-add-apartment',
  templateUrl: './add-apartment.component.html',
  styleUrls: ['./add-apartment.component.scss']
})
export class AddApartmentComponent {

  isUserLoggedIn = false;
  addApartForm;
  user: UserDTO;
  apartmentResult$: Observable<string | undefined>;

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
    if (this.addApartForm.valid) {
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
            const apartmentData: Apartment = {
              dailyPrice: parseInt(this.addApartForm.value.dailyPrice!),
              title: this.addApartForm.value.title!,
              country: this.addApartForm.value.country!,
              city: this.addApartForm.value.city!,
              street: this.addApartForm.value.street!,
              buildingNumber: this.addApartForm.value.buildingNumber!,
              apartmentNumber: this.addApartForm.value.apartmentNumber!,
            };
            return this.apartmentService.addApartment(this.user, apartmentData);
          })
        )
        .subscribe(
          {
          next: response =>{
            this.addApartForm.reset();
            this.messageService.add({
              severity: 'success',
              summary: 'Apartment added correctly',
              detail: '',
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
