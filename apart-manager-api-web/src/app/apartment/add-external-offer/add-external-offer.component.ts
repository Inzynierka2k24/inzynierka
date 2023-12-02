import { Component } from '@angular/core';
import {Apartment, ExternalOffer, UserDTO} from "../../../generated";
import {Observable} from "rxjs";
import {AbstractControl, FormBuilder, FormGroup, ValidatorFn, Validators} from "@angular/forms";
import {Store} from "@ngrx/store";
import {AppState} from "../../core/store/app.store";
import {ExternalOfferService} from "../services/external-offer.service";
import {Router} from "@angular/router";
import {MessageService} from "primeng/api";
import {selectCurrentUser} from "../../core/store/user/user.selectors";
import {switchMap} from "rxjs/operators";
import {ApartmentService} from "../services/apartment.service";

@Component({
  selector: 'app-add-external-offer',
  templateUrl: './add-external-offer.component.html',
  styleUrls: ['./add-external-offer.component.scss']
})
export class AddExternalOfferComponent {
  apartments: Apartment[] = [];
  apartment: Apartment;
  isUserLoggedIn = false;
  addExternalOfferForm;
  user: UserDTO;
  user$: Observable<UserDTO | undefined>;
  externalOfferResult$: Observable<string | undefined>;

  constructor(private formBuilder: FormBuilder,
              private store: Store<AppState>,
              private externalOfferService: ExternalOfferService,
              private apartmentService: ApartmentService,
              private router: Router,
              private messageService: MessageService){

    this.addExternalOfferForm = formBuilder.nonNullable.group(
      {
        serviceType: ['', [Validators.required]],
        externalLink: ['', [Validators.required, this.urlValidator()]],
        apartmentId: ['', [Validators.required]]
      })
  }

  ngOnInit() {
    this.fetchData();
  }

  fetchData(){
    this.user$ = this.store.select(selectCurrentUser);
    this.user$.subscribe((user) => {
      if (user) {
        this.apartmentService.getApartments(user).subscribe((data: Apartment[]) => {
          this.apartments = data;
        });
      }
    });
  }

  addExternalOffer(): void {
    // if (this.addExternalOfferForm.valid) {
    //   this.store
    //     .select(selectCurrentUser)
    //     .pipe(
    //       switchMap((user) => {
    //         if (!user) {
    //           this.isUserLoggedIn = false;
    //           throw new Error('User not logged in');
    //         }
    //         this.isUserLoggedIn = true;
    //         this.user = user;
    //         const externalOfferData: ExternalOffer = {
    //           serviceType: this.addExternalOfferForm.value.serviceType!,
    //           externalLink: this.addExternalOfferForm.value.externalLink!,
    //           apartmentId: this.addExternalOfferForm.value.apartmentId.id!,
    //         };
    //         return this.externalOfferService.addExternalOffer(this.user, externalOfferData, { responseType: 'text' });
    //       })
    //     )
    //     .subscribe(
    //       {
    //       next: response =>{
    //         this.addExternalOfferForm.reset();
    //         this.messageService.add({
    //           severity: 'success',
    //           summary: 'ExternalOffer added correctly',
    //           detail: 'success'
    //         })},
    //       error:error => {
    //           console.error('API call error:', error);
    //         }
    //       },
    //     );
    // } else {
    //   this.messageService.add({
    //     severity: 'error',
    //     summary: 'Validation Error',
    //     detail: 'Please fill in all required fields and correct validation errors.',
    //   });
    //   this.markAllFieldsAsTouched(this.addExternalOfferForm);
    // }
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

   urlValidator(): ValidatorFn {
    return (control: AbstractControl): { [key: string]: any } | null => {
      const urlPattern = /^(https?|ftp):\/\/[^\s/$.?#].[^\s]*$/i;
      const isValid = urlPattern.test(control.value);
      return isValid ? null : { 'invalidUrl': { value: control.value } };
    };
  }
}
