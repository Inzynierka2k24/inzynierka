import { createReducer, on } from '@ngrx/store';
import UserActions from './user.actions';
import { initialState } from './user.store';

export const userReducer = createReducer(
  initialState,
  on(UserActions.login, UserActions.register, (state, action) => ({
    ...state,
    loading: true,
    user: { mail: action.mail },
  })),
  on(UserActions.loginComplete, (state, action) => ({
    ...state,
    loading: false,
  })),
  on(UserActions.loginError, UserActions.registerError, (state, error) => ({
    ...state,
    loading: false,
    error,
  })),
  on(UserActions.registerComplete, (state) => ({ ...state, loading: false })),
);
