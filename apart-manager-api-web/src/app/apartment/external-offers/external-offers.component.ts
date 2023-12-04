import { Component } from '@angular/core';
import {selectCurrentUser} from "../../core/store/user/user.selectors";
import {Apartment, ExternalOffer, UserDTO} from "../../../generated";
import {forkJoin, Observable, of} from "rxjs";
import {Message, MessageService} from "primeng/api";
import {FormBuilder, FormGroup} from "@angular/forms";
import {Store} from "@ngrx/store";
import {AppState} from "../../core/store/app.store";
import {ExternalOfferService} from "../services/external-offer.service";
import {Router} from "@angular/router";
import {ApartmentService} from "../services/apartment.service";
import { switchMap, map, mergeAll, toArray } from 'rxjs/operators';

@Component({
  selector: 'app-external-offers',
  templateUrl: './external-offers.component.html',
  styleUrls: ['./external-offers.component.scss']
})
export class ExternalOffersComponent {
  apartments: Apartment[] = [];
  apartment: Apartment;
  externalOffers: { [apartmentId: number]: ExternalOffer[] } = {};
  externalOffersList: ExternalOffer[] = [];
  externalOffersFlatten: any = [];
  externalOffer: ExternalOffer;
  messages: Message[] = [];
  isEditing = true;
  editExternalOfferForm: FormGroup;
  user$: Observable<UserDTO | undefined>;
  user: UserDTO;

  constructor(private store: Store<AppState>,
            private externalOfferService: ExternalOfferService,
            private apartmentService: ApartmentService,
            private messageService: MessageService,
            private router: Router,
            private fb: FormBuilder) {

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


  addExternalOffer() {
    this.router.navigate(['/apartments/externalOffers/add']);
  }

  startEditing(externalOfferId: number, apartmentId: number) {
    this.router.navigate(['/apartments/externalOffers/edit', externalOfferId, apartmentId]);
    // this.router.navigate(['/apartments/externalOffers/edit',
    //   [this.getExternalOfferById(externalOfferId), apartmentId]]);
  }


  deleteExternalOffer(): void {
    console.log('elele')

  }
  // deleteExternalOffer(externalOfferId: number, apartmentId: number): void {
  //   console.log('elele')
  //   this.store
  //     .select(selectCurrentUser)
  //     .pipe(
  //       switchMap((user) => {
  //         if (!user) {
  //           throw new Error('User not logged in');
  //         }
  //         this.user = user;
  //         const deletedApartment = this.getApartmentById(apartmentId);
  //         const deletedOffer = this.getExternalOfferById(externalOfferId);
  //
  //         if (!deletedApartment || !deletedOffer) {
  //           throw new Error('Apartment and External Offer cannot be null');
  //         }
  //         return this.externalOfferService.deleteExternalOffer(
  //           this.user,
  //           deletedApartment,
  //           deletedOffer,
  //           { responseType: 'text' }
  //         );
  //       })
  //     )
  //     .subscribe(
  //       {
  //       next: response =>{
  //         this.editExternalOfferForm.reset();
  //         this.messageService.add({
  //           severity: 'success',
  //           summary: 'External Offer deleted correctly',
  //           detail: 'success'
  //         });
  //         this.fetchData();
  //       },
  //       error:error => {
  //           console.error('API call error:', error);
  //           this.fetchData();
  //         },
  //       },
  //     );
  // }

  // flattenExternalOffers(): any[] {
  //   const result: any[] = [];
  //   console.log(this.externalOffers)
  //   for (const apartmentId in this.externalOffers) {
  //     const externalOffersForApartment = this.externalOffers[apartmentId];
  //     externalOffersForApartment.forEach((externalOffer:any) => {
  //       result.push({ apartmentId: apartmentId, ...externalOffer });
  //     });
  //   }
  //   console.log(result)
  //   return result;
  // }

  getApartmentById(id: number): Apartment | null{
    for (const apartment of this.apartments){
      if (apartment.id == id){
        return apartment;
      }
    }
    return null;
  }

  getExternalOfferById(id: number): ExternalOffer | null{
    for (const offer of this.externalOffersList){
      if (offer.id == id){
        return offer;
      }
    }
    return null;
  }
}
