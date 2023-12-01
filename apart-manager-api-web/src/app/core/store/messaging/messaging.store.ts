import { ContactDTO } from '../../../../generated';

export interface MessagingState {
  loading: boolean;
  contacts?: ContactDTO[];
  error?: any;
}

export const initialState: MessagingState = {
  loading: false,
  contacts: undefined,
  error: undefined,
};

export enum MessagingActionTypes {
  LOAD_CLIENTS = '[MESSAGING] Load Clients Action',
  LOAD_CLIENTS_COMPLETE = '[MESSAGING] Load Clients Action Complete',
  LOAD_CLIENTS_ERROR = '[MESSAGING] Load Clients Action Error',
  ADD_ORDER = '[MESSAGING] Add Order Action',
  ADD_ORDER_COMPLETE = '[MESSAGING] Add Order Action Complete',
  ADD_ORDER_ERROR = '[MESSAGING] Add Order Action Error',
}
