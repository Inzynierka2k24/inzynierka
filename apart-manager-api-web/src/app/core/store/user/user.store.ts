import { UserDTO } from '../../../../generated';

export interface UserState {
  loading: boolean;
  user?: UserDTO;
  error?: any;
}

export const initialState: UserState = {
  loading: false,
  user: undefined,
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
}
