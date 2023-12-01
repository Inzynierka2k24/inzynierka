import { Apartment } from '../../../../generated';

export interface ApartmentState {
  apartmentList: Apartment[];
}

export const initialState: ApartmentState = {
  apartmentList: [],
};

export enum ApartmentActionTypes {
  ADD = '[Apartment] Add Action',
  ADD_COMPLETE = '[Apartment] Add Action Complete',
  ADD_ERROR = '[Apartment] Add Action Error',
  DELETE = '[Apartment] Delete Action',
  DELETE_COMPLETE = '[Apartment] Delete Action Complete',
  DELETE_ERROR = '[Apartment] Delete Action Error',
  FETCH = '[Apartment] Fetch Action',
  FETCH_COMPLETE = '[Apartment] Fetch Action Complete',
  FETCH_ERROR = '[Apartment] Fetch Action Error',
}
