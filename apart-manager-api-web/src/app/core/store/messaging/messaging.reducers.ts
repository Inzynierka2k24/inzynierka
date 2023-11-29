import { createReducer, on } from '@ngrx/store';
import { initialState } from './messaging.store';
import MessagingActions from './messaging.actions';

export const messagingReducer = createReducer(
  initialState,
  on(MessagingActions.loadContacts, (state) => ({
    ...state,
    loading: true,
  })),
  on(MessagingActions.loadContactsComplete, (state, action) => ({
    ...state,
    loading: false,
    contacts: action.contacts,
  })),
  on(MessagingActions.loadContactsError, (state, action) => ({
    ...state,
    loading: false,
    error: action,
  })),
);
