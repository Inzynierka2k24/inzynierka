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
import {switchMap} from "rxjs/operators";

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
  user$: Observable<UserDTO | undefined>;
  user: UserDTO;

  constructor(private store: Store<AppState>,
              private apartmentService: ApartmentService,
              private messageService: MessageService,
              private router: Router,
              private fb: FormBuilder) {

    this.editApartmentForm = this.fb.group({
        dailyPrice: ['', [Validators.required, Validators.min(0)]],
        title: ['', [Validators.required]],
        country: ['', [Validators.required]],
        city: ['', [Validators.required]],
        street: ['', [Validators.required]],
        buildingNumber: ['', [Validators.required, Validators.min(0)]],
        apartmentNumber: ['', [Validators.required, Validators.min(0)]],
    });
  }

  ngOnInit() {
    this.fetchData();
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

  deleteApartment(apartment: Apartment): void {
    this.store
      .select(selectCurrentUser)
      .pipe(
        switchMap((user) => {
          if (!user) {
            throw new Error('User not logged in');
          }
          this.user = user;
          // return this.apartmentService.deleteApartment(this.user, <number>apartment.id);
          return this.apartmentService.deleteApartment(this.user, <number>apartment.id);
        })
      )
      .subscribe(
        {
        next: response =>{
          this.editApartmentForm.reset();
          this.messageService.add({
            severity: 'success',
            summary: 'Apartment deleted successfully',
            detail: '',
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

  cleanMessages() {
    setTimeout(() => {
      this.messages = [];
    }, 3000);
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
}
