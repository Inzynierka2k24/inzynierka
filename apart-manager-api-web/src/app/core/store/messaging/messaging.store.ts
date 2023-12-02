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
  ADD_CONTACT = '[MESSAGING] Add Contact Action',
  ADD_CONTACT_COMPLETE = '[MESSAGING] Add Contact Action Complete',
  ADD_CONTACT_ERROR = '[MESSAGING] Add Contact Action Error',
  DELETE_MESSAGE = '[MESSAGING] Delete Message Action',
  DELETE_MESSAGE_COMPLETE = '[MESSAGING] Delete Message Action Complete',
  DELETE_MESSAGE_ERROR = '[MESSAGING] Delete Message Action Error',
  DELETE_CONTACT = '[MESSAGING] Delete Contact Action',
  DELETE_CONTACT_COMPLETE = '[MESSAGING] Delete Contact Action Complete',
  DELETE_CONTACT_ERROR = '[MESSAGING] Delete Contact Action Error',
}
