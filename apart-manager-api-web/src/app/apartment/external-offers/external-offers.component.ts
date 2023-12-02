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
  externalOffers: ExternalOffer[] = [];
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

  fetchData(){
    this.user$ = this.store.select(selectCurrentUser);
    this.user$.subscribe((user) => {
      if (user) {
        // this.externalOfferService.getExternalOffers(user).subscribe((data: ExternalOffer[]) => {
        //   this.externalOffers = data;
        // });
        this.apartmentService.getApartments(user).subscribe((data: Apartment[]) => {
          this.apartments = data;
        });
      }
    });

    //todo fetch external offers
  }


  addExternalOffer() {
    this.router.navigate(['/apartments/externalOffers/add']);
  }

  startEditing(externalOffer: ExternalOffer) {
    this.router.navigate(['/apartments/externalOffers/edit', externalOffer]);
  }

  deleteExternalOffer(externalOffer: ExternalOffer): void {
    // this.store
    //   .select(selectCurrentUser)
    //   .pipe(
    //     switchMap((user) => {
    //       if (!user) {
    //         throw new Error('User not logged in');
    //       }
    //       this.user = user;
    //       return this.externalOfferService.deleteExternalOffer(this.user, <number>externalOffer.id, { responseType: 'text' });
    //     })
    //   )
    //   .subscribe(
    //     {
    //     next: response =>{
    //       this.editExternalOfferForm.reset();
    //       this.messageService.add({
    //         severity: 'success',
    //         summary: 'External Offer deleted correctly',
    //         detail: 'success'
    //       });
    //       this.fetchData();
    //     },
    //     error:error => {
    //         console.error('API call error:', error);
    //         this.fetchData();
    //       },
    //     },
    //   );
  }
}
