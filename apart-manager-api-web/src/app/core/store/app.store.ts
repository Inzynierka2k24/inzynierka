import { StoreModule } from '@ngrx/store';
import { UserState } from './user/user.store';
import { userReducer } from './user/user.reducers';
import { NgModule } from '@angular/core';
import { EffectsModule } from '@ngrx/effects';
import {
  edit,
  getDetails,
  login,
  loginComplete,
  register,
} from './user/user.effects';
import { apartmentReducer } from './apartment/apartment.reducer';

export interface AppState {
  user: UserState;
}

@NgModule({
  imports: [
    StoreModule.forRoot({
      user: userReducer,
      apartmentEntries: apartmentReducer,
    }),
    EffectsModule.forRoot({ login, loginComplete, register, getDetails, edit }),
  ],
  exports: [StoreModule, EffectsModule],
})
export class AppStoreModule {}
