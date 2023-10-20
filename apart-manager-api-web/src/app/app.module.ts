import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppComponent } from './app.component';
import { SharedModule } from './core/shared.module';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { AppRoutingModule } from './app-routing.module';
import { HeaderModule } from './header/header.module';
import { DashboardModule } from './dashboard/dashboard.module';
import { ToastModule } from 'primeng/toast';
import { WelcomePageComponent } from './welcome-page/welcome-page.component';
import {StoreModule} from "@ngrx/store";
import {apartmentReducer} from "./core/store/apartment/apartment.reducer";

@NgModule({
  declarations: [AppComponent, WelcomePageComponent],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    AppRoutingModule,
    SharedModule,
    HeaderModule,
    DashboardModule,
    ToastModule,
    StoreModule.forRoot({apartmentEntries: apartmentReducer}),
  ],
  providers: [],
  bootstrap: [AppComponent],
})
export class AppModule {}
