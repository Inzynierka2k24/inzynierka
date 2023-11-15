import {createAction, createReducer, on, props} from "@ngrx/store";
import { Reservation } from "../../../generated";
import {ReservationActionTypes} from "./reservation.store";
import {addReservation, deleteReservation} from "./reservation.actions";

export const initialReservationEntries: Reservation[] = [];

export const reservationReducer = createReducer(
  initialReservationEntries,

  on(addReservation, (entries, reservation) => {
    const entriesClone: Reservation[] = JSON.parse(JSON.stringify(entries));
    entriesClone.push(reservation);
    return entriesClone;
  }),

  on(deleteReservation, (entries, reservation) => {
    const entriesClone: Reservation[] = JSON.parse(JSON.stringify(entries));
    const found = entriesClone.find(e => e.id == reservation.id);
    if (found){
      entriesClone.splice(entriesClone.indexOf(found), 1)
    }
    return entriesClone;
  }),
)
