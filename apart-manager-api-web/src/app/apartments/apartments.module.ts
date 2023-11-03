import { NgModule } from '@angular/core';
import { AddApartmentComponent } from './add-apartment/add-apartment.component';
import { ApartmentListComponent } from './apartment-list/apartment-list.component';
import { SharedModule } from '../core/shared.module';
import { ReactiveFormsModule } from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { RippleModule } from 'primeng/ripple';

@NgModule({
  declarations: [AddApartmentComponent, ApartmentListComponent],
  imports: [
    SharedModule,
    ReactiveFormsModule,
    InputTextModule,
    ButtonModule,
    RippleModule,
  ],
})
export class ApartmentsModule {}
