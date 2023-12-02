import { AppState } from '../app.store';
import { createSelector } from '@ngrx/store';
import { MessagingState } from './messaging.store';

export const messagingStateSelector = (state: AppState) => state.messaging;

export const selectMessagingLoadingState = createSelector(
  messagingStateSelector,
  (state: MessagingState) => state.loading,
);

export const selectMessagingList = createSelector(
  messagingStateSelector,
  (state: MessagingState) => state.contacts,
);

export const selectMessagingStateError = createSelector(
  messagingStateSelector,
  (state: MessagingState) => state.error,
);
