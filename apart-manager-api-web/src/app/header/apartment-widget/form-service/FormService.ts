import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class FormService {
  formVisible = false;
  listVisible = false;

  toggleFormVisibility() {
    if (this.listVisible) this.listVisible = false;
    this.formVisible = !this.formVisible;
  }

  toggleListVisibility() {
    if (this.formVisible) this.formVisible = false;
    this.listVisible = !this.listVisible;
  }
}
