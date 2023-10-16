import { NgModule } from '@angular/core';
import { SharedModule } from '../core/shared.module';
import { DashboardComponent } from './dashboard.component';
import {HeaderModule} from "../header/header.module";

@NgModule({
  declarations: [DashboardComponent],
  imports: [SharedModule, HeaderModule],
})
export class DashboardModule {}
