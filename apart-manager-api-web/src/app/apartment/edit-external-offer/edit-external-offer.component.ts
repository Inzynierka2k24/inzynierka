import { Component } from '@angular/core';
import {Apartment, ExternalOffer, ExternalService, UserDTO} from "../../../generated";
import {Observable, of} from "rxjs";
import {AbstractControl, FormBuilder, FormGroup, ValidatorFn, Validators} from "@angular/forms";
import {Store} from "@ngrx/store";
import {AppState} from "../../core/store/app.store";
import {ExternalOfferService} from "../services/external-offer.service";
import {ApartmentService} from "../services/apartment.service";
import {ActivatedRoute, Router} from "@angular/router";
import {MessageService} from "primeng/api";
import {selectCurrentUser} from "../../core/store/user/user.selectors";
import {map, switchMap} from "rxjs/operators";
import { forkJoin } from 'rxjs';

@Component({
  selector: 'app-edit-external-offer',
  templateUrl: './edit-external-offer.component.html',
  styleUrls: ['./edit-external-offer.component.scss']
})
export class EditExternalOfferComponent {
  apartments: Apartment[] = [];
  apartment: Apartment;
  isUserLoggedIn = false;
  editExternalOfferForm;
  externalOffers: { [apartmentId: number]: ExternalOffer[] } = {};
  externalOffersList: ExternalOffer[] = [];
  externalOffer: ExternalOffer;
  user: UserDTO;
  user$: Observable<UserDTO | undefined>;
  externalOfferResult$: Observable<string | undefined>;
  externalOfferId: number;
  apartmentId: number;
  externalOffersFlatten: any = [];

  constructor(private formBuilder: FormBuilder,
              private store: Store<AppState>,
              private externalOfferService: ExternalOfferService,
              private apartmentService: ApartmentService,
              private router: Router,
              private messageService: MessageService,
              private route: ActivatedRoute,){

    this.editExternalOfferForm = formBuilder.nonNullable.group(
      {
        serviceType: ['', [Validators.required]],
        externalLink: ['', [Validators.required, this.urlValidator()]],
        apartmentId: ['', [Validators.required]]
      });
  }

  ngOnInit() {
    this.fetchData();
  }


  fetchData(): void {
    this.user$ = this.store.select(selectCurrentUser);
    this.user$.pipe(
      switchMap((user) => {
        if (!user) {
          throw new Error('User not logged in');
        }
        this.user = user;
        return this.apartmentService.getApartments(user);
      }),
      switchMap((apartments: Apartment[]) => {
        this.apartments = apartments;
        this.externalOffers = {};
        this.externalOffersList = [];

        const externalOffersObservables = this.apartments.map((apart) =>
          this.externalOfferService.getExternalOffers(this.user, apart).pipe(
            map((data: ExternalOffer[]) => {
              if (apart.id !== undefined) {
                this.externalOffers[apart.id] = data;
                this.externalOffersList = this.externalOffersList.concat(data);
              }
              return data;
            })
          )
        );

        return externalOffersObservables.length > 0
          ? forkJoin(externalOffersObservables)
          : of([]);
      })
    ).subscribe(() => {
      this.externalOffersFlatten = this.flattenExternalOffers();
      this.externalOfferId = +this.route.snapshot.paramMap.get('externalOfferId')!;
      this.externalOffer = <ExternalOffer>this.getExternalOfferById(this.externalOfferId);
      this.apartmentId = +this.route.snapshot.paramMap.get('apartmentId')!;

      this.editExternalOfferForm.setValue({
        serviceType: this.externalOffer.serviceType,
        externalLink: this.externalOffer.externalLink,
        apartmentId: this.apartmentId as unknown as string
      });
    });
  }

  flattenExternalOffers(): any[] {
    const result: any[] = [];
    for (const apartmentId in this.externalOffers) {
      const externalOffersForApartment = this.externalOffers[apartmentId];
      externalOffersForApartment.forEach((externalOffer: any) => {
        result.push({ apartmentId: apartmentId, ...externalOffer });
      });
    }
    return result;
  }

  editExternalOffer(): void {
    if (this.editExternalOfferForm.valid) {
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
            const externalOfferData: ExternalOffer = {
              id: this.externalOfferId,
              serviceType: this.editExternalOfferForm.value.serviceType! as ExternalService,
              externalLink: this.editExternalOfferForm.value.externalLink!,
            };
            if (this.editExternalOfferForm.value.apartmentId! == null){
              this.editExternalOfferForm.controls['apartmentId'].setValue(this.apartmentId.toString());
            }
            return this.externalOfferService.updateExternalOffer(
              this.user,
              <Apartment>this.getApartmentById(this.editExternalOfferForm.value.apartmentId! as unknown as number),
              externalOfferData,
              { responseType: 'text' }
            );
          })
        )
        .subscribe(
          {
          next: response =>{
            this.editExternalOfferForm.reset();
            this.messageService.add({
              severity: 'success',
              summary: 'External Offer edited correctly',
              detail: 'success'
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
      this.markAllFieldsAsTouched(this.editExternalOfferForm);
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

  urlValidator(): ValidatorFn {
    return (control: AbstractControl): { [key: string]: any } | null => {
      const urlPattern = /^(https?|ftp):\/\/[^\s/$.?#].[^\s]*$/i;
      const isValid = urlPattern.test(control.value);
      return isValid ? null : { 'invalidUrl': { value: control.value } };
    };
  }

  getApartmentById(id: number): Apartment | null{
    for (const apartment of this.apartments){
      if (apartment.id == id){
        return apartment;
      }
    }
    return null;
  }

  getExternalOfferById(id: number): ExternalOffer | null {
    console.log(this.externalOffersList)
    for (const offer of this.externalOffersList) {
      console.log(offer)
      if (offer.id === id) {
        return offer;
      }
    }
    return null;
  }

  cancelEditing(): void {
    window.history.back()
  }
}
