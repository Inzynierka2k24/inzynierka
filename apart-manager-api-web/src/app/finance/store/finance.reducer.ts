import {createAction, createReducer, on, props} from "@ngrx/store";
import { Finance } from "../../../generated";
import {FinanceActionTypes} from "./finance.store";
import {addFinance, deleteFinance} from "./finance.actions";

export const initialFinanceEntries: Finance[] = [];

export const financeReducer = createReducer(
  initialFinanceEntries,

  on(addFinance, (entries, finance) => {
    const entriesClone: Finance[] = JSON.parse(JSON.stringify(entries));
    entriesClone.push(finance);
    return entriesClone;
  }),

  on(deleteFinance, (entries, finance) => {
    const entriesClone: Finance[] = JSON.parse(JSON.stringify(entries));
    const found = entriesClone.find(e => e.financeNumber == finance.financeNumber);
    if (found){
      entriesClone.splice(entriesClone.indexOf(found), 1)
    }
    return entriesClone;
  }),
)
