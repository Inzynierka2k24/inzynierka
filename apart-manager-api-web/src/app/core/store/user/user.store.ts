import { User } from '../../../../generated';

export interface UserState {
  loading: boolean;
  user?: User;
  error?: Error;
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
  REGISTER = '[User] Register Action',
  REGISTER_COMPLETE = '[User] Register Action Complete',
  REGISTER_ERROR = '[User] Register Action Error',
}
