import { createReducer } from '@ngrx/store';
import { initialState } from './messaging.store';

export const messagingReducer = createReducer(initialState);
