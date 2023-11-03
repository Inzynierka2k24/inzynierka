import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppComponent } from './app.component';
import { SharedModule } from './core/shared.module';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { AppInterceptor, AppRoutingModule } from './app-routing.module';
import { HeaderModule } from './header/header.module';
import { ToastModule } from 'primeng/toast';
import { WelcomePageComponent } from './welcome-page/welcome-page.component';
import {StoreModule} from "@ngrx/store";
import {apartmentReducer} from "./core/store/apartment/apartment.reducer";
import {FormsModule} from "@angular/forms";
import { FullCalendarModule } from '@fullcalendar/angular';

import { PreferencesComponent } from './user/preferences/preferences.component';
import { ButtonModule } from 'primeng/button';
import { RippleModule } from 'primeng/ripple';
import { InputTextModule } from 'primeng/inputtext';
import { ReactiveFormsModule } from '@angular/forms';
import { CheckboxModule } from 'primeng/checkbox';
import { DashboardComponent } from './dashboard/dashboard.component';
import { HTTP_INTERCEPTORS } from '@angular/common/http';

@NgModule({
  declarations: [
    AppComponent,
    WelcomePageComponent,
    PreferencesComponent,
    DashboardComponent,
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    AppRoutingModule,
    SharedModule,
    HeaderModule,
    ToastModule,
    ButtonModule,
    RippleModule,
    InputTextModule,
    ReactiveFormsModule,
    CheckboxModule,
    StoreModule.forRoot({apartmentEntries: apartmentReducer}),
    FormsModule,
    FullCalendarModule,

  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AppInterceptor,
      multi: true,
    },
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}
