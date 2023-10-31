import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppComponent } from './app.component';
import { SharedModule } from './core/shared.module';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { AppRoutingModule } from './app-routing.module';
import { HeaderModule } from './header/header.module';
import { ToastModule } from 'primeng/toast';
import { WelcomePageComponent } from './welcome-page/welcome-page.component';
import {StoreModule} from "@ngrx/store";
import {apartmentReducer} from "./core/store/apartment/apartment.reducer";
import {FormsModule} from "@angular/forms";
import { FullCalendarModule } from '@fullcalendar/angular';


@NgModule({
  declarations: [AppComponent, WelcomePageComponent],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    AppRoutingModule,
    SharedModule,
    HeaderModule,
    ToastModule,
    StoreModule.forRoot({apartmentEntries: apartmentReducer}),
    FormsModule,
    FullCalendarModule,

  ],
  providers: [],
  bootstrap: [AppComponent],
})
export class AppModule {}
