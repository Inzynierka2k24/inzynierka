import { Actions, createEffect, ofType } from '@ngrx/effects';
import { inject } from '@angular/core';
import MessagingActions from './messaging.actions';
import { catchError, exhaustMap, map, of } from 'rxjs';
import { MessagingService } from '../../../messaging/messaging.service';

export const loadContacts = createEffect(
  ($actions = inject(Actions), messagingService = inject(MessagingService)) => {
    return $actions.pipe(
      ofType(MessagingActions.loadContacts),
      exhaustMap((action) => {
        return messagingService.getContactsForUser(action.userId).pipe(
          map((contacts) =>
            MessagingActions.loadContactsComplete({ contacts }),
          ),
          catchError((err) => of(MessagingActions.loadContactsError(err))),
        );
      }),
    );
  },
  { functional: true },
);

export const addOrder = createEffect(
  ($actions = inject(Actions), messagingService = inject(MessagingService)) => {
    return $actions.pipe(
      ofType(MessagingActions.addOrder),
      exhaustMap((action) => {
        return messagingService.addOrder(action.userId, action.message).pipe(
          map(() => MessagingActions.addOrderComplete()),
          catchError((err) => of(MessagingActions.addOrderError(err))),
        );
      }),
    );
  },
  { functional: true },
);
