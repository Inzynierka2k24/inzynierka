import { AppState } from '../app.store';
import { createSelector } from '@ngrx/store';
import { UserState } from './user.store';

export const userStateSelector = (state: AppState) => state.user;

export const selectUserLoadingState = createSelector(
  userStateSelector,
  (state: UserState) => state.loading,
);

export const selectCurrentUser = createSelector(
  userStateSelector,
  (state: UserState) => state.user,
);

export const selectUserStateError = createSelector(
  userStateSelector,
  (state: UserState) => state.error,
);
