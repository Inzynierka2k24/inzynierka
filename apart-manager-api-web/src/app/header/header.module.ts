import { NgModule } from '@angular/core';
import { InputTextModule } from 'primeng/inputtext';
import { PasswordModule } from 'primeng/password';
import { SharedModule } from '../core/shared.module';
import { ReactiveFormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { UserWidgetComponent } from './user-widget/user-widget.component';
import { LoginComponent } from './user-widget/components/login/login.component';
import { HeaderComponent } from './header.component';
import { MenubarModule } from 'primeng/menubar';
import { RippleModule } from 'primeng/ripple';
import { CheckboxModule } from 'primeng/checkbox';
import { RegisterComponent } from './user-widget/components/register/register.component';
import { MessagesModule } from 'primeng/messages';
import { AddApartmentComponent } from './apartment-widget/add-apartment/add-apartment.component';
import { FormServiceComponent } from './apartment-widget/form-service/form-service.component';
import { ApartmentListComponent } from './apartment-widget/apartment-list/apartment-list.component';
import { DividerModule } from 'primeng/divider';
import { UserCardComponent } from './user-widget/components/user-card/user-card.component';
import { MenuModule } from 'primeng/menu';

@NgModule({
  declarations: [
    LoginComponent,
    UserWidgetComponent,
    HeaderComponent,
    RegisterComponent,
    AddApartmentComponent,
    FormServiceComponent,
    ApartmentListComponent,
    UserCardComponent,
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
  ],
  exports: [HeaderComponent, AddApartmentComponent, ApartmentListComponent],
})
export class HeaderModule {}
