import { NgModule } from '@angular/core';
import { InputTextModule } from 'primeng/inputtext';
import { PasswordModule } from 'primeng/password';
import { SharedModule } from '../core/shared.module';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { UserWidgetComponent } from './user-widget/user-widget.component';
import { LoginComponent } from './user-widget/components/login/login.component';
import { HeaderComponent } from './header.component';
import { MenubarModule } from 'primeng/menubar';
import { RippleModule } from 'primeng/ripple';
import { CheckboxModule } from 'primeng/checkbox';
import { RegisterComponent } from './user-widget/components/register/register.component';
import { MessagesModule } from 'primeng/messages';
import { AddApartmentComponent } from '../apartment/add-apartment/add-apartment.component';
import { ApartmentListComponent } from '../apartment/apartment-list/apartment-list.component';
import { DividerModule } from 'primeng/divider';
import { ReservationCalendarComponent } from '../reservation/reservation-calendar/reservation-calendar.component';
import { FullCalendarModule } from "@fullcalendar/angular";
import { AddReservationComponent } from '../reservation/add-reservation/add-reservation.component';
import { CalendarModule } from 'primeng/calendar';
import { TieredMenuModule } from 'primeng/tieredmenu';
import { TableModule } from "primeng/table";
import {ReservationListComponent} from "../reservation/reservation-list/reservation-list.component";
import {EditApartmentComponent} from "../apartment/edit-apartment/edit-apartment.component";
import {EditReservationComponent} from "../reservation/edit-reservation/edit-reservation.component";
import {AddFinanceComponent} from "../finance/add-finance/add-finance.component";
import {FinanceListComponent} from "../finance/finance-list/finance-list.component";
import {DropdownModule} from "primeng/dropdown";
import {EditFinanceComponent} from "../finance/edit-finance/edit-finance.component";
import { UserCardComponent } from './user-widget/components/user-card/user-card.component';
import { MenuModule } from 'primeng/menu';
import { ChartModule } from 'primeng/chart';
import {FinanceChartComponent} from "../finance/finance-chart/finance-chart.component";
import {TabView, TabViewModule} from "primeng/tabview";

@NgModule({
  declarations: [
    LoginComponent,
    UserWidgetComponent,
    HeaderComponent,
    RegisterComponent,
    AddApartmentComponent,
    ApartmentListComponent,
    UserCardComponent,
    ReservationCalendarComponent,
    AddReservationComponent,
    ReservationListComponent,
    EditApartmentComponent,
    EditReservationComponent,
    AddFinanceComponent,
    FinanceListComponent,
    EditFinanceComponent,
    UserCardComponent,
    FinanceChartComponent,

  ],
  imports: [
    SharedModule,
    ReactiveFormsModule,
    InputTextModule,
    PasswordModule,
    ButtonModule,
    MenubarModule,
    RippleModule,
    CheckboxModule,
    MessagesModule,
    DividerModule,
    MenuModule,
    FullCalendarModule,
    CalendarModule,
    TieredMenuModule,
    TableModule,
    DropdownModule,
    FormsModule,
    ChartModule,
    TabViewModule,

  ],
  exports: [
    HeaderComponent,
    AddApartmentComponent,
    ApartmentListComponent,
    ReservationListComponent,
    ReservationCalendarComponent,
    EditReservationComponent
  ],
})
export class HeaderModule {}
