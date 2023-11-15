import { Component, OnInit } from '@angular/core';
import { ApartmentService } from '../services/apartment.service';
import {Apartment, UserDTO} from '../../../generated';
import { Observable } from 'rxjs';
import { MessageService } from 'primeng/api';
import { Message } from 'primeng/api';
import {Router} from "@angular/router";
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import {selectCurrentUser} from "../../core/store/user/user.selectors";
import {Store} from "@ngrx/store";
import {AppState} from "../../core/store/app.store";

@Component({
  selector: 'app-apartment-list',
  templateUrl: './apartment-list.component.html',
  styleUrls: ['./apartment-list.component.scss']
})
export class ApartmentListComponent implements OnInit {
  apartments: Apartment[] = [];
  apartment: Apartment;
  countApartments$: Observable<number>;
  messages: Message[] = [];
  isEditing = true;
  editApartmentForm: FormGroup;
  user: UserDTO;
  isUserLoggedIn = false;
  displayContent = false;
  formToggle = false;

  constructor(private store: Store<AppState>,
              private apartmentService: ApartmentService,
              private messageService: MessageService,
              private router: Router,
              private fb: FormBuilder) {

    this.editApartmentForm = this.fb.group({
      dailyPrice: [null, Validators.required],
      title: ['', Validators.required],
      country: ['', Validators.required],
      city: ['', Validators.required],
      street: ['', Validators.required],
      buildingNumber: ['', Validators.required],
      apartmentNumber: ['', Validators.required],
    });
  }
  ngOnInit() {

    const userDataSub = this.store
      .select(selectCurrentUser)
      .subscribe((user) => {
        if (user) {
          this.isUserLoggedIn = true;
          this.user = user;

          this.apartmentService.getApartments(this.user).subscribe((data: Apartment[]) => {
          this.apartments = data;
          });
        } else {
          this.isUserLoggedIn = false;
        }
    });
  }

  addApartment() {
    this.router.navigate(['/apartments/add']);
  }

  startEditing(apartment: Apartment) {
    this.router.navigate(['/apartments/edit', apartment]);
  }

  saveChanges(apartment: Apartment) {
    console.log('Edit Apartment:', event);
    this.messages = [{
      severity: 'success',
      detail: 'Apartment edited successfully',
    }];
    this.cleanMessages();
  }


  deleteApartment(event: any) {
    console.log('Delete Apartment:', event);
    this.messages = [{
      severity: 'info',
      detail: 'Apartment deleted successfully',
    }];
    this.cleanMessages();
  }

  cleanMessages() {
    setTimeout(() => {
      this.messages = [];
    }, 3000);
  }
}
