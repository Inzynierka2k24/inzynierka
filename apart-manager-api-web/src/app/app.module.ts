import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppComponent } from './app.component';
import { SharedModule } from './core/shared.module';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { AppInterceptor, AppRoutingModule } from './app-routing.module';
import { HeaderModule } from './header/header.module';
import { WelcomePageComponent } from './welcome-page/welcome-page.component';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { ReservationsModule } from './resevations/reservations.module';
import { ApartmentsModule } from './apartments/apartments.module';
import { UserModule } from './user/user.module';
import { ToastModule } from 'primeng/toast';

@NgModule({
  declarations: [AppComponent, WelcomePageComponent],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    AppRoutingModule,
    SharedModule,
    HeaderModule,
    ReservationsModule,
    ApartmentsModule,
    UserModule,
    ToastModule,
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
