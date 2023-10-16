import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class FormService {
  formVisible = false;

  toggleFormVisibility() {
    this.formVisible = !this.formVisible;
  }
}
