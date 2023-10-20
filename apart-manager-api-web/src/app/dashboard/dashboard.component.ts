import { Component } from '@angular/core';
import {FormService} from "../header/apartment-widget/form-service/FormService";

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent {
  constructor(private formService: FormService) {}

  get formVisible() {
    return this.formService.formVisible;
  }
  get listVisible() {
    return this.formService.listVisible;
  }
}
