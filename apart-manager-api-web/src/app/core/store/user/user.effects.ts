import { Actions, createEffect, ofType } from '@ngrx/effects';
import { AuthService } from '../../services/auth.service';
import { inject } from '@angular/core';
import { catchError, exhaustMap, map, of } from 'rxjs';
import UserActions from './user.actions';

export const login = createEffect(
  (actions$ = inject(Actions), authService = inject(AuthService)) => {
    return actions$.pipe(
      ofType(UserActions.login),
      exhaustMap((action) =>
        authService
          .login({ mail: action.mail, password: action.password })
          .pipe(
            map(() => UserActions.loginComplete()),
            catchError((error) => of(UserActions.loginError(error))),
          ),
      ),
    );
  },
  { functional: true },
);

export const register = createEffect(
  (actions$ = inject(Actions), authService = inject(AuthService)) => {
    return actions$.pipe(
      ofType(UserActions.register),
      exhaustMap((action) =>
        authService
          .register({
            mail: action.mail,
            password: action.password,
          })
          .pipe(
            map(() => UserActions.registerComplete()),
            catchError((error) => of(UserActions.registerError(error))),
          ),
      ),
    );
  },
  { functional: true },
);
