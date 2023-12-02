import { Component } from '@angular/core';
import {selectCurrentUser} from "../../core/store/user/user.selectors";
import {Apartment, ExternalOffer, UserDTO} from "../../../generated";
import {Observable} from "rxjs";
import {Message, MessageService} from "primeng/api";
import {FormBuilder, FormGroup} from "@angular/forms";
import {Store} from "@ngrx/store";
import {AppState} from "../../core/store/app.store";
import {ExternalOfferService} from "../services/external-offer.service";
import {Router} from "@angular/router";
import {switchMap} from "rxjs/operators";
import {ApartmentService} from "../services/apartment.service";

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

  fetchData() {
    this.user$ = this.store.select(selectCurrentUser);
    this.user$.subscribe((user) => {
      if (user) {
        this.apartmentService.getApartments(user).subscribe((apartments: Apartment[]) => {
          this.apartments = apartments;
          this.externalOffers = {};
          this.externalOffersList = [];

          for (const apart of this.apartments) {

            this.externalOfferService.getExternalOffers(user, apart).subscribe((data: ExternalOffer[]) => {
               if (apart.id !== undefined){
                this.externalOffers[apart.id] = data;
                this.externalOffersList.concat(data);
                }
            });
          }
        });
      }
    });
  }



  addExternalOffer() {
    this.router.navigate(['/apartments/externalOffers/add']);
  }

  startEditing(externalOfferId: number, apartmentId: number) {
    this.router.navigate(['/apartments/externalOffers/edit',
      [this.getExternalOfferById(externalOfferId), apartmentId]]);
  }

  deleteExternalOffer(externalOfferId: number, apartmentId: number): void {
    this.store
      .select(selectCurrentUser)
      .pipe(
        switchMap((user) => {
          if (!user) {
            throw new Error('User not logged in');
          }
          this.user = user;
          const deletedApartment = this.getApartmentById(apartmentId);
          const deletedOffer = this.getExternalOfferById(externalOfferId);

          if (!deletedApartment || !deletedOffer) {
            throw new Error('Apartment and External Offer cannot be null');
          }
          return this.externalOfferService.deleteExternalOffer(
            this.user,
            deletedApartment,
            deletedOffer,
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
            summary: 'External Offer deleted correctly',
            detail: 'success'
          });
          this.fetchData();
        },
        error:error => {
            console.error('API call error:', error);
            this.fetchData();
          },
        },
      );
  }

  flattenExternalOffers(): any[] {
    const result: any[] = [];

    for (const apartmentId in this.externalOffers) {
      if (this.externalOffers.hasOwnProperty(apartmentId)) {
        const externalOffersForApartment = this.externalOffers[apartmentId];
        externalOffersForApartment.forEach((externalOffer:any) => {
          result.push({ apartmentId: apartmentId, ...externalOffer });
        });
      }
    }
    return result;
  }

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
