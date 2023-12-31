import { createAction, props } from '@ngrx/store';
import { Apartment } from '../../../../generated';
import { ApartmentActionTypes } from './apartment.store';

export const addApartment = createAction(
  ApartmentActionTypes.ADD,
  props<Apartment>(),
);

const addApartmentComplete = createAction(
  ApartmentActionTypes.ADD_COMPLETE,
  props<Apartment>(),
);

const addApartmentError = createAction(
  ApartmentActionTypes.ADD_ERROR,
  props<Apartment>(),
);

export const deleteApartment = createAction(
  ApartmentActionTypes.DELETE,
  props<Apartment>(),
);

const deleteApartmentComplete = createAction(
  ApartmentActionTypes.DELETE_COMPLETE,
  props<Apartment>(),
);

const deleteApartmentError = createAction(
  ApartmentActionTypes.DELETE_ERROR,
  props<Apartment>(),
);

const fetchApartments = createAction(ApartmentActionTypes.FETCH);
const fetchApartmentsComplete = createAction(
  ApartmentActionTypes.FETCH_COMPLETE,
  props<{ apartments: Apartment[] }>(),
);
const fetchApartmentsError = createAction(
  ApartmentActionTypes.FETCH,
  props<Error>(),
);

const ApartmentActions = {
  addApartment,
  addApartmentComplete,
  addApartmentError,
  deleteApartment,
  deleteApartmentComplete,
  deleteApartmentError,
  fetchApartments,
  fetchApartmentsComplete,
  fetchApartmentsError,
};

export default ApartmentActions;
