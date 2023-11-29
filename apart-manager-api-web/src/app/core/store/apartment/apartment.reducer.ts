import { createReducer, on } from '@ngrx/store';
import { initialState } from './apartment.store';
import { addApartment } from './apartment.actions';

export const apartmentReducer = createReducer(
  initialState,

  on(addApartment, (state, apartment) => {
    return { ...state, apartmentList: [...state.apartmentList, apartment] };
  }),
  //
  // on(deleteApartment, (entries, apartment) => {
  //   const entriesClone: Apartment[] = JSON.parse(JSON.stringify(entries));
  //   const found = entriesClone.find(
  //     (e) => e.apartmentNumber == apartment.apartmentNumber,
  //   );
  //   if (found) {
  //     entriesClone.splice(entriesClone.indexOf(found), 1);
  //   }
  //   return entriesClone;
  // }),
);
