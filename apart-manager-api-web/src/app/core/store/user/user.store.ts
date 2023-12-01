import { ExternalAccount, UserDTO } from '../../../../generated';

export interface UserState {
  loading: boolean;
  user?: UserDTO;
  externalAccounts?: ExternalAccount[];
  error?: any;
}

export const initialState: UserState = {
  loading: false,
  user: undefined,
  externalAccounts: undefined,
  error: undefined,
};

export enum UserActionTypes {
  LOGIN = '[User] Login Action',
  LOGIN_COMPLETE = '[User] Login Action Complete',
  LOGIN_ERROR = '[User] Login Action Error',
  LOGOUT = '[User] Logout Action',
  REGISTER = '[User] Register Action',
  REGISTER_COMPLETE = '[User] Register Action Complete',
  REGISTER_ERROR = '[User] Register Action Error',
  EDIT = '[User] Edit Action',
  EDIT_COMPLETE = '[User] Edit Action Complete',
  EDIT_ERROR = '[User] Edit Action Error',
  DETAILS = '[User] Details Action',
  DETAILS_COMPLETE = '[User] Details Action Complete',
  DETAILS_ERROR = '[User] Details Action Error',
  DELETE = '[User] Delete Action',
  DELETE_COMPLETE = '[User] Delete Action Complete',
  DELETE_ERROR = '[User] Delete Action Error',
}
