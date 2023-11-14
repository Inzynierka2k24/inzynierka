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

@NgModule({
  declarations: [DashboardComponent, PreferencesComponent],
  imports: [
    SharedModule,
    ReactiveFormsModule,
    CheckboxModule,
    InputTextModule,
    ButtonModule,
    RippleModule,
    ConfirmDialogModule,
    PasswordModule,
  ],
  providers: [ConfirmationService],
})
export class UserModule {}
