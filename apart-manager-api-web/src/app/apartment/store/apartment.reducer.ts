import {createAction, createReducer, on, props} from "@ngrx/store";
import { Apartment } from "../../../generated";
import {ApartmentActionTypes} from "./apartment.store";
import {addApartment, deleteApartment} from "./apartment.actions";

export const initialApartmentEntries: Apartment[] = [];

export const apartmentReducer = createReducer(
  initialApartmentEntries,

  on(addApartment, (entries, apartment) => {
    const entriesClone: Apartment[] = JSON.parse(JSON.stringify(entries));
    entriesClone.push(apartment);
    return entriesClone;
  }),

  on(deleteApartment, (entries, apartment) => {
    const entriesClone: Apartment[] = JSON.parse(JSON.stringify(entries));
    const found = entriesClone.find(e => e.apartmentNumber == apartment.apartmentNumber);
    if (found){
      entriesClone.splice(entriesClone.indexOf(found), 1)
    }
    return entriesClone;
  }),
)
