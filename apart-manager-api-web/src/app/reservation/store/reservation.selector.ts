import { Reservation } from "../../../generated";
import {createFeatureSelector, createSelector} from "@ngrx/store";

export const selectCountReservations = createSelector(
  createFeatureSelector('apartmentEntries'),
  (state: Reservation[]) => {
    return state.length;
  }
);

export const selectTotalCount = createSelector(
    createFeatureSelector('apartmentEntries'),
    (state: Reservation[]) => {
      let totalCount = 0;
      state.forEach(() => totalCount += 1);
      return totalCount;
    }
);

