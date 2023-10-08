import { Component, Input } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Store } from '@ngrx/store';
import { AppState } from '../../../../core/store/app.store';
import { Observable } from 'rxjs';
import { selectUserLoadingState } from '../../../../core/store/user/user.selectors';
import UserActions from '../../../../core/store/user/user.actions';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
})
export class LoginComponent {
  @Input()
  toggleForm: () => void;

  isLoading$: Observable<boolean>;
  loginForm: FormGroup;

  constructor(
    private store: Store<AppState>,
    private formBuilder: FormBuilder,
  ) {
    this.loginForm = formBuilder.nonNullable.group({
      login: ['', Validators.required],
      password: ['', Validators.required],
    });
    this.isLoading$ = store.select(selectUserLoadingState);
  }

  login() {
    if (this.loginForm.valid) {
      this.store.dispatch(
        UserActions.login({
          login: this.loginForm.controls['login'].value,
          password: this.loginForm.controls['password'].value,
        }),
      );
    }
  }
}
