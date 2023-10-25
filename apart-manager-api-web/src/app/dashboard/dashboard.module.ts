import { NgModule } from '@angular/core';
import { SharedModule } from '../core/shared.module';
import { DashboardComponent } from './dashboard.component';
import {HeaderModule} from "../header/header.module";
import {RouterModule} from "@angular/router";

@NgModule({
  declarations: [DashboardComponent],
  imports: [SharedModule, HeaderModule, RouterModule],
})
export class DashboardModule {}
