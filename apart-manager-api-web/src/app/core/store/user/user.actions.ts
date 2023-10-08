import { createAction, props } from '@ngrx/store';
import { LoginRequest, RegisterRequest, User } from '../../../../generated';
import { UserActionTypes } from './user.store';

/**
 * [Use Case] Login
 */
const login = createAction(UserActionTypes.LOGIN, props<LoginRequest>());
const loginComplete = createAction(
  UserActionTypes.LOGIN_COMPLETE,
  props<User>(),
);
const loginError = createAction(UserActionTypes.LOGIN_ERROR, props<Error>());

/**
 * [Use Case] Registration
 */
const register = createAction(
  UserActionTypes.REGISTER,
  props<RegisterRequest>(),
);
const registerComplete = createAction(UserActionTypes.REGISTER_COMPLETE);
const registerError = createAction(
  UserActionTypes.REGISTER_ERROR,
  props<Error>(),
);

const UserActions = {
  login,
  loginError,
  loginComplete,
  register,
  registerError,
  registerComplete,
};

export default UserActions;
