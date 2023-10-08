import { Component, Input } from '@angular/core';
import { Observable } from 'rxjs';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Store } from '@ngrx/store';
import { AppState } from '../../../../core/store/app.store';
import { selectUserLoadingState } from '../../../../core/store/user/user.selectors';
import UserActions from '../../../../core/store/user/user.actions';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss'],
})
export class RegisterComponent {
  @Input()
  toggleForm: () => void;

  isLoading$: Observable<boolean>;
  registrationForm: FormGroup;

  constructor(
    private store: Store<AppState>,
    private formBuilder: FormBuilder,
  ) {
    this.registrationForm = formBuilder.nonNullable.group({
      login: ['', Validators.required],
      password: ['', Validators.required],
      mail: ['', [Validators.required, Validators.email]],
    });
    this.isLoading$ = store.select(selectUserLoadingState);
  }

  register() {
    if (this.registrationForm.valid) {
      this.store.dispatch(
        UserActions.register({
          login: this.registrationForm.controls['login'].value,
          password: this.registrationForm.controls['password'].value,
          mail: this.registrationForm.controls['mail'].value,
        }),
      );
    }
  }
}
