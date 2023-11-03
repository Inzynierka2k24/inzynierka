import { createReducer, on } from '@ngrx/store';
import UserActions from './user.actions';
import { initialState } from './user.store';

export const userReducer = createReducer(
  initialState,
  on(
    UserActions.login,
    UserActions.register,
    UserActions.edit,
    UserActions.details,
    (state) => ({
      ...state,
      loading: true,
    }),
  ),
  on(
    UserActions.loginComplete,
    UserActions.registerComplete,
    UserActions.editComplete,
    (state, user) => ({
      ...state,
      loading: false,
    }),
  ),
  on(
    UserActions.loginError,
    UserActions.registerError,
    UserActions.editError,
    UserActions.detailsError,
    (state, error) => ({
      ...state,
      loading: false,
      error,
    }),
  ),
  on(UserActions.detailsComplete, (state, user) => ({
    ...state,
    loading: false,
    user,
  })),
  on(UserActions.logout, () => ({
    ...initialState,
  })),
);
