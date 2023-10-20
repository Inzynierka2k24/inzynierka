import {createAction, props} from '@ngrx/store';
import {AuthRequest} from '../../../../generated';
import {UserActionTypes} from './user.store';

/**
 * [Use Case] Login
 */
const login = createAction(UserActionTypes.LOGIN, props<AuthRequest>());
const loginComplete = createAction(UserActionTypes.LOGIN_COMPLETE);
const loginError = createAction(UserActionTypes.LOGIN_ERROR, props<Error>());
const logout = createAction(UserActionTypes.LOGOUT);
/**
 * [Use Case] Registration
 */
const register = createAction(UserActionTypes.REGISTER, props<AuthRequest>());
const registerComplete = createAction(UserActionTypes.REGISTER_COMPLETE);
const registerError = createAction(
  UserActionTypes.REGISTER_ERROR,
  props<Error>(),
);

const UserActions = {
  login,
  loginError,
  loginComplete,
  logout,
  register,
  registerError,
  registerComplete,
};

export default UserActions;
