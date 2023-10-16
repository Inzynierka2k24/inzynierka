import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DashboardComponent } from './dashboard/dashboard.component';
import {AddApartmentComponent} from "./header/apartment-widget/add-apartment/add-apartment.component";

const routes: Routes = [
  { path: '**', component: DashboardComponent },
  { path: 'apartment/add', component:AddApartmentComponent},

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
