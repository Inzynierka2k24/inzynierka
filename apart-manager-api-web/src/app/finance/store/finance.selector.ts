import { Finance } from "../../../generated";
import {createFeatureSelector, createSelector} from "@ngrx/store";

export const selectCountFinances = createSelector(
  createFeatureSelector('financeEntries'),
  (state: Finance[]) => {
    return state.length;
  }
);

export const selectTotalCount = createSelector(
    createFeatureSelector('financeEntries'),
    (state: Finance[]) => {
      var totalCount = 0;
      state.forEach(() => totalCount += 1);
      return totalCount;
    }
);

