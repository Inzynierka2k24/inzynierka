import { Apartment } from "../../../generated";
import {createFeatureSelector, createSelector} from "@ngrx/store";

export const selectCountApartments = createSelector(
  createFeatureSelector('apartmentEntries'),
  (state: Apartment[]) => {
    return state.length;
  }
);

export const selectTotalCount = createSelector(
    createFeatureSelector('apartmentEntries'),
    (state: Apartment[]) => {
      var totalCount = 0;
      state.forEach(() => totalCount += 1);
      return totalCount;
    }
);

