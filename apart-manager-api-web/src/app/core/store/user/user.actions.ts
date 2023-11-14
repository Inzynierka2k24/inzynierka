import { createAction, props } from '@ngrx/store';
import {
  AuthRequest,
  EditUserRequest,
  RegisterRequest,
  UserDTO,
} from '../../../../generated';
import { UserActionTypes } from './user.store';

/**
 * [Use Case] Login
 */
const login = createAction(UserActionTypes.LOGIN, props<AuthRequest>());
const loginComplete = createAction(
  UserActionTypes.LOGIN_COMPLETE,
  props<{ jwt: string }>(),
);
const loginError = createAction(UserActionTypes.LOGIN_ERROR, props<Error>());
const logout = createAction(UserActionTypes.LOGOUT);
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
/**
 * [Use Case] Edit
 */
const edit = createAction(UserActionTypes.EDIT, props<EditUserActionProps>());
const editComplete = createAction(
  UserActionTypes.EDIT_COMPLETE,
  props<EditUserRequest>(),
);
const editError = createAction(UserActionTypes.EDIT_ERROR, props<Error>());
/**
 * [Use Case] User Details
 */
const details = createAction(UserActionTypes.DETAILS);
const detailsComplete = createAction(
  UserActionTypes.DETAILS_COMPLETE,
  props<UserDTO>(),
);
const detailsError = createAction(
  UserActionTypes.DETAILS_ERROR,
  props<Error>(),
);
/**
 * [Use Case] User Deletion
 */
const deleteUser = createAction(UserActionTypes.DELETE);
const deleteUserComplete = createAction(UserActionTypes.DELETE_COMPLETE);
const deleteUserError = createAction(UserActionTypes.DELETE_ERROR);

const UserActions = {
  login,
  loginError,
  loginComplete,
  logout,
  register,
  registerError,
  registerComplete,
  edit,
  editComplete,
  editError,
  details,
  detailsComplete,
  detailsError,
  deleteUser,
  deleteUserComplete,
  deleteUserError,
};

export default UserActions;

export interface EditUserActionProps {
  userId: number;
  editUserRequest: EditUserRequest;
}
