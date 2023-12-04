import { StoreModule } from '@ngrx/store';
import { UserState } from './user/user.store';
import { userReducer } from './user/user.reducers';
import { NgModule } from '@angular/core';
import { EffectsModule } from '@ngrx/effects';
import UserEffects from './user/user.effects';
import { MessagingState } from './messaging/messaging.store';
import { messagingReducer } from './messaging/messaging.reducers';
import { apartmentReducer } from './apartment/apartment.reducers';
import MessagingEffects from './messaging/messaging.effects';
import { ApartmentState } from './apartment/apartment.store';

export interface AppState {
  user: UserState;
  messaging: MessagingState;
  apartments: ApartmentState;
}

@NgModule({
  imports: [
    StoreModule.forRoot({
      user: userReducer,
      apartments: apartmentReducer,
      messaging: messagingReducer,
    }),
    EffectsModule.forRoot({
      ...UserEffects,
      ...MessagingEffects,
    }),
  ],
  exports: [StoreModule, EffectsModule],
})
export class AppStoreModule {}
