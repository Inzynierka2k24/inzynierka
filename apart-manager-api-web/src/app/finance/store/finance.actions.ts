import { createAction, props } from "@ngrx/store";
import { Finance } from "../../../generated";
import { FinanceActionTypes } from "./finance.store";

export const addFinance = createAction(FinanceActionTypes.ADD, props<Finance>());

const addFinanceComplete = createAction(
  FinanceActionTypes.ADD_COMPLETE,
  props<Finance>(),
);

const addFinanceError = createAction(
  FinanceActionTypes.ADD_ERROR,
  props<Finance>(),
);

export const deleteFinance = createAction(FinanceActionTypes.DELETE, props<Finance>());

const deleteFinanceComplete = createAction(
  FinanceActionTypes.DELETE_COMPLETE,
  props<Finance>(),
);

const deleteFinanceError = createAction(
  FinanceActionTypes.DELETE_ERROR,
  props<Finance>(),
);

const FinanceActions = {
  addFinance,
  addFinanceComplete,
  addFinanceError,
  deleteFinance,
  deleteFinanceComplete,
  deleteFinanceError,
};

export default FinanceActions;
