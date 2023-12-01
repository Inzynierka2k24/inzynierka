import { NgModule } from '@angular/core';
import { DashboardComponent } from './dashboard/dashboard.component';
import { PreferencesComponent } from './preferences/preferences.component';
import { SharedModule } from '../core/shared.module';
import { ReactiveFormsModule } from '@angular/forms';
import { CheckboxModule } from 'primeng/checkbox';
import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { RippleModule } from 'primeng/ripple';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ConfirmationService } from 'primeng/api';
import { PasswordModule } from 'primeng/password';
import { TableModule } from 'primeng/table';
import { AddExternalAccountModalComponent } from './preferences/add-external-account-modal/add-external-account-modal.component';
import { DialogModule } from 'primeng/dialog';
import { DropdownModule } from 'primeng/dropdown';
import { RadioButtonModule } from 'primeng/radiobutton';
import { NgOptimizedImage } from '@angular/common';

@NgModule({
  declarations: [
    DashboardComponent,
    PreferencesComponent,
    AddExternalAccountModalComponent,
  ],
  imports: [
    SharedModule,
    ReactiveFormsModule,
    CheckboxModule,
    InputTextModule,
    ButtonModule,
    RippleModule,
    ConfirmDialogModule,
    PasswordModule,
    TableModule,
    DialogModule,
    DropdownModule,
    RadioButtonModule,
    NgOptimizedImage,
  ],
  providers: [ConfirmationService],
})
export class UserModule {}
