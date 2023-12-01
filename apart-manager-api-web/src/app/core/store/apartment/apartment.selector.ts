import { createSelector } from '@ngrx/store';
import { AppState } from '../app.store';
import { ApartmentState } from './apartment.store';

export const apartmentStateSelector = (state: AppState) => state.apartments;

export const selectApartmentList = createSelector(
  apartmentStateSelector,
  (state: ApartmentState) => {
    return state.apartmentList;
  },
);
