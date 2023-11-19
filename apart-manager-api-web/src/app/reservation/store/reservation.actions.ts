import { createAction, props } from "@ngrx/store";
import { Reservation } from "../../../generated";
import {ReservationActionTypes} from "./reservation.store";

export const addReservation = createAction(ReservationActionTypes.ADD, props<Reservation>());

const addReservationComplete = createAction(
  ReservationActionTypes.ADD_COMPLETE,
  props<Reservation>(),
);

const addReservationError = createAction(
  ReservationActionTypes.ADD_ERROR,
  props<Reservation>(),
);

export const editReservation = createAction(ReservationActionTypes.EDIT, props<Reservation>());

const editReservationComplete = createAction(
  ReservationActionTypes.EDIT_COMPLETE,
  props<Reservation>(),
);

const editReservationError = createAction(
  ReservationActionTypes.EDIT_ERROR,
  props<Reservation>(),
);

export const deleteReservation = createAction(ReservationActionTypes.DELETE, props<Reservation>());

const deleteReservationComplete = createAction(
  ReservationActionTypes.DELETE_COMPLETE,
  props<Reservation>(),
);

const deleteReservationError = createAction(
  ReservationActionTypes.DELETE_ERROR,
  props<Reservation>(),
);

const ReservationActions = {
  addReservation,
  addReservationComplete,
  addReservationError,
  editReservation,
  editReservationComplete,
  editReservationError,
  deleteReservation,
  deleteReservationComplete,
  deleteReservationError,
};

export default ReservationActions;
