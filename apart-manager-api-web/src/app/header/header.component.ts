import { Component } from '@angular/core';
import {Router} from "@angular/router";
import {FormService} from "./apartment-widget/form-service/FormService";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent {
  constructor(private router: Router, private formService: FormService) {}

   navigateToAddApartment(): void {
    this.formService.toggleFormVisibility();
  }

}
