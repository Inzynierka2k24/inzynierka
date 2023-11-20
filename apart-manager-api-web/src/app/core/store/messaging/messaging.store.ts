import { Contact } from '../../../../generated';

export interface MessagingState {
  loading: boolean;
  contacts?: Contact[];
  error?: any;
}

export const initialState: MessagingState = {
  loading: false,
  contacts: undefined,
  error: undefined,
};

export enum MessagingActionTypes {}
