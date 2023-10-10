import { NgModule } from '@angular/core';
import { SharedModule } from '../core/shared.module';
import { DashboardComponent } from './dashboard.component';

@NgModule({
  declarations: [DashboardComponent],
  imports: [SharedModule],
})
export class DashboardModule {}
