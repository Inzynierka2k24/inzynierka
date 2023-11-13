import { Actions, createEffect, ofType } from '@ngrx/effects';
import { AuthService } from '../../services/auth.service';
import { inject } from '@angular/core';
import { catchError, exhaustMap, map, of, tap } from 'rxjs';
import UserActions from './user.actions';
import { Router } from '@angular/router';
import { UserService } from '../../../user/services/user.service';
import { LocalStorageService } from '../../services/local-storage.service';

export const login = createEffect(
  (actions$ = inject(Actions), authService = inject(AuthService)) => {
    return actions$.pipe(
      ofType(UserActions.login),
      exhaustMap((action) =>
        authService
          .login({
            login: action.login,
            password: action.password,
          })
          .pipe(
            map((jwt) => UserActions.loginComplete({ jwt })),
            catchError((error) => of(UserActions.loginError(error))),
          ),
      ),
    );
  },
  { functional: true },
);

export const loginComplete = createEffect(
  (
    actions$ = inject(Actions),
    localStorageService = inject(LocalStorageService),
    router = inject(Router),
  ) => {
    return actions$.pipe(
      ofType(UserActions.loginComplete),
      tap(({ jwt }) => {
        localStorageService.saveData('JWT_TOKEN', jwt);
        router.navigate(['user', 'dashboard']);
      }),
      map(() => UserActions.details()),
    );
  },
  { functional: true },
);

export const logout = createEffect(
  (
    actions$ = inject(Actions),
    localStorageService = inject(LocalStorageService),
    router = inject(Router),
  ) => {
    return actions$.pipe(
      ofType(UserActions.logout),
      tap(() => {
        localStorageService.removeData('JWT_TOKEN');
        router.navigate(['']);
      }),
    );
  },
  { functional: true, dispatch: false },
);

export const register = createEffect(
  (actions$ = inject(Actions), authService = inject(AuthService)) => {
    return actions$.pipe(
      ofType(UserActions.register),
      exhaustMap((action) =>
        authService
          .register({
            emailAddress: action.emailAddress,
            login: action.login,
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

export const edit = createEffect(
  (actions$ = inject(Actions), userService = inject(UserService)) => {
    return actions$.pipe(
      ofType(UserActions.edit),
      exhaustMap(({ userId, editUserRequest }) =>
        userService.editUser(userId, editUserRequest).pipe(
          map((user) => UserActions.editComplete(user)),
          catchError((error) => of(UserActions.editError(error))),
        ),
      ),
    );
  },
  { functional: true },
);

export const getDetails = createEffect(
  (
    actions$ = inject(Actions),
    userService = inject(UserService),
    localStorageService = inject(LocalStorageService),
  ) => {
    return actions$.pipe(
      ofType(UserActions.details),
      exhaustMap(() =>
        userService.getUserDetails().pipe(
          map((user) => UserActions.detailsComplete(user)),
          catchError((error) => {
            localStorageService.removeData('JWT_TOKEN');
            return of(UserActions.detailsError(error));
          }),
        ),
      ),
    );
  },
  { functional: true },
);
