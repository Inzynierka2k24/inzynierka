import { Component, Input } from '@angular/core';
import { Observable } from 'rxjs';
import {
  AbstractControl,
  FormBuilder,
  ValidationErrors,
  ValidatorFn,
  Validators,
} from '@angular/forms';
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
  registrationForm;

  constructor(
    private store: Store<AppState>,
    private formBuilder: FormBuilder,
  ) {
    this.registrationForm = formBuilder.nonNullable.group(
      {
        login: ['', Validators.required],
        password: ['', [Validators.required, Validators.minLength(5)]],
        passwordRepeat: ['', Validators.required],
        emailAddress: ['', [Validators.required, Validators.email]],
      },
      {
        validators: matchingPasswordValidator,
        updateOn: 'blur',
      },
    );
    this.isLoading$ = store.select(selectUserLoadingState);
  }

  register() {
    if (this.registrationForm.valid) {
      this.store.dispatch(
        UserActions.register({
          login: this.registrationForm.value.login!,
          password: this.registrationForm.value.password!,
          emailAddress: this.registrationForm.value.emailAddress!,
        }),
      );
    }
  }
}

/**
 * @param control AbstractControl to provide validation for.
 * @returns ValidationErrors
 * with property passwordsNotMatching set to true when passwords don't match.
 * @returns null
 * when passwords match.
 */
const matchingPasswordValidator: ValidatorFn = (
  control: AbstractControl,
): ValidationErrors | null => {
  const passwordValue = control.value?.password;
  const passwordRepeatValue = control.value?.passwordRepeat;

  return passwordValue &&
    passwordRepeatValue &&
    passwordValue == passwordRepeatValue
    ? null
    : { passwordsNotMatching: true };
};
