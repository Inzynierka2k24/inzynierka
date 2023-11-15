import { createAction, props } from "@ngrx/store";
import { Apartment } from "../../../generated";
import { ApartmentActionTypes } from "./apartment.store";

export const addApartment = createAction(ApartmentActionTypes.ADD, props<Apartment>());

const addApartmentComplete = createAction(
  ApartmentActionTypes.ADD_COMPLETE,
  props<Apartment>(),
);

const addApartmentError = createAction(
  ApartmentActionTypes.ADD_ERROR,
  props<Apartment>(),
);

export const deleteApartment = createAction(ApartmentActionTypes.DELETE, props<Apartment>());

const deleteApartmentComplete = createAction(
  ApartmentActionTypes.DELETE_COMPLETE,
  props<Apartment>(),
);

const deleteApartmentError = createAction(
  ApartmentActionTypes.DELETE_ERROR,
  props<Apartment>(),
);

const ApartmentActions = {
  addApartment,
  addApartmentComplete,
  addApartmentError,
  deleteApartment,
  deleteApartmentComplete,
  deleteApartmentError,
};

export default ApartmentActions;
