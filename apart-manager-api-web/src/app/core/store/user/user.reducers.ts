import { createReducer, on } from '@ngrx/store';
import UserActions from './user.actions';
import { initialState } from './user.store';

export const userReducer = createReducer(
  initialState,
  on(UserActions.login, UserActions.register, (state) => ({
    ...state,
    loading: true,
  })),
  on(UserActions.loginComplete, (state, user) => ({
    ...state,
    loading: false,
    user,
  })),
  on(UserActions.loginError, UserActions.registerError, (state, error) => ({
    ...state,
    loading: false,
    error,
  })),
  on(UserActions.registerComplete, (state) => ({ ...state, loading: false })),
);
