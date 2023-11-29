import { Component } from '@angular/core';
import { Apartment, UserDTO } from '../../../generated';
import { Message, MessageService } from 'primeng/api';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ApartmentService } from '../services/apartment.service';
import { ActivatedRoute, Router } from '@angular/router';
import { selectCurrentUser } from '../../core/store/user/user.selectors';
import { switchMap } from 'rxjs/operators';
import { Store } from '@ngrx/store';
import { AppState } from '../../core/store/app.store';

@Component({
  selector: 'app-edit-apartment',
  templateUrl: './edit-apartment.component.html',
  styleUrls: ['./edit-apartment.component.scss'],
})
export class EditApartmentComponent {
  apartment: any;
  messages: Message[] = [];
  editApartmentForm: FormGroup;
  isUserLoggedIn = false;
  user: UserDTO;

  constructor(
    private apartmentService: ApartmentService,
    private messageService: MessageService,
    private router: Router,
    private store: Store<AppState>,
    private fb: FormBuilder,
    private route: ActivatedRoute,
  ) {
    this.route.params.subscribe((params) => {
      this.apartment = params;
    });

    this.editApartmentForm = this.fb.group({
      dailyPrice: ['', [Validators.required, Validators.min(0)]],
      title: ['', [Validators.required]],
      country: ['', [Validators.required]],
      city: ['', [Validators.required]],
      street: ['', [Validators.required]],
      buildingNumber: ['', [Validators.required, Validators.min(0)]],
      apartmentNumber: ['', [Validators.required, Validators.min(0)]],
      rating: [0, [Validators.required, Validators.min(0), Validators.max(5)]],
    });

    this.editApartmentForm.setValue({
      dailyPrice: this.apartment.dailyPrice,
      title: this.apartment.title,
      country: this.apartment.country,
      city: this.apartment.city,
      street: this.apartment.street,
      buildingNumber: this.apartment.buildingNumber,
      apartmentNumber: this.apartment.apartmentNumber,
      rating: this.apartment.rating,
    });
  }

  editApartment(): void {
    //todo
    if (this.editApartmentForm.valid) {
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
              dailyPrice: parseInt(this.editApartmentForm.value.dailyPrice!),
              title: this.editApartmentForm.value.title!,
              country: this.editApartmentForm.value.country!,
              city: this.editApartmentForm.value.city!,
              street: this.editApartmentForm.value.street!,
              buildingNumber: this.editApartmentForm.value.buildingNumber!,
              apartmentNumber: this.editApartmentForm.value.apartmentNumber!,
              rating: this.editApartmentForm.value.rating!,
            };
            return this.apartmentService.updateApartment(
              this.user,
              apartmentData,
            );
          }),
        )
        .subscribe({
          next: (response) => {
            this.editApartmentForm.reset();
            this.messageService.add({
              severity: 'success',
              summary: 'Apartment edited correctly',
              detail: '',
            });
          },
          error: (error) => {
            console.error('API call error:', error);
          },
        });
    } else {
      this.messageService.add({
        severity: 'error',
        summary: 'Validation Error',
        detail:
          'Please fill in all required fields and correct validation errors.',
      });
      this.markAllFieldsAsTouched(this.editApartmentForm);
    }
  }

  cancelEditing() {
    this.router.navigate(['/apartments']);
  }

  cleanMessages() {
    setTimeout(() => {
      this.messages = [];
    }, 3000);
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
