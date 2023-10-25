import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { WelcomePageComponent } from './welcome-page/welcome-page.component';
import {DashboardComponent} from "./dashboard/dashboard.component";
import {AddApartmentComponent} from "./header/apartment-widget/add-apartment/add-apartment.component";
import {ApartmentListComponent} from "./header/apartment-widget/apartment-list/apartment-list.component";
import {ReservationListComponent} from "./header/reservation-widget/reservation-list/reservation-list.component";

// const routes: Routes = [{ path: '**', component: WelcomePageComponent }];

const routes: Routes = [
  {path: 'apartments/add', component: AddApartmentComponent},
  {path: 'apartments', component: ApartmentListComponent},
  {path: 'reservations', component: ReservationListComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
