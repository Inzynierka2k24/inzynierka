import { Actions, createEffect, ofType } from '@ngrx/effects';
import { inject } from '@angular/core';
import MessagingActions from './messaging.actions';
import { catchError, exhaustMap, map, of } from 'rxjs';
import { MessagingService } from '../../../messaging/messaging.service';

const loadContacts = createEffect(
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

const addContact = createEffect(
  ($actions = inject(Actions), messagingService = inject(MessagingService)) => {
    return $actions.pipe(
      ofType(MessagingActions.addContact),
      exhaustMap((action) => {
        return messagingService.addContact(action.userId, action.contact).pipe(
          map(() =>
            MessagingActions.addContactComplete({ contact: action.contact }),
          ),
          catchError((err) => of(MessagingActions.addContactError(err))),
        );
      }),
    );
  },
  { functional: true },
);

const deleteContact = createEffect(
  ($actions = inject(Actions), messagingService = inject(MessagingService)) => {
    return $actions.pipe(
      ofType(MessagingActions.deleteContact),
      exhaustMap((action) => {
        return messagingService
          .deleteContact(action.userId, action.contactId)
          .pipe(
            map(() =>
              MessagingActions.deleteContactComplete({
                contactId: action.contactId,
              }),
            ),
            catchError((err) => of(MessagingActions.deleteContactError(err))),
          );
      }),
    );
  },
  { functional: true },
);

const addOrder = createEffect(
  ($actions = inject(Actions), messagingService = inject(MessagingService)) => {
    return $actions.pipe(
      ofType(MessagingActions.addOrder),
      exhaustMap((action) => {
        return messagingService
          .addOrder(action.userId, action.contactId, action.message)
          .pipe(
            map(() =>
              MessagingActions.addOrderComplete({
                contactId: action.contactId,
                message: action.message,
              }),
            ),
            catchError((err) => of(MessagingActions.addOrderError(err))),
          );
      }),
    );
  },
  { functional: true },
);

const deleteMessage = createEffect(
  ($actions = inject(Actions), messagingService = inject(MessagingService)) => {
    return $actions.pipe(
      ofType(MessagingActions.deleteMessage),
      exhaustMap((action) => {
        return messagingService
          .deleteMessage(action.userId, action.contactId, action.messageId)
          .pipe(
            map(() =>
              MessagingActions.deleteMessageComplete({
                contactId: action.contactId,
                messageId: action.messageId,
              }),
            ),
            catchError((err) => of(MessagingActions.deleteMessageError(err))),
          );
      }),
    );
  },
  { functional: true },
);

const editContact = createEffect(
  ($actions = inject(Actions), messagingService = inject(MessagingService)) => {
    return $actions.pipe(
      ofType(MessagingActions.editContact),
      exhaustMap((action) => {
        return messagingService.editContact(action.userId, action.contact).pipe(
          map(() =>
            MessagingActions.editContactComplete({
              contact: action.contact,
            }),
          ),
          catchError((err) => of(MessagingActions.editContactError(err))),
        );
      }),
    );
  },
  { functional: true },
);

export default {
  loadContacts,
  addContact,
  deleteContact,
  addOrder,
  deleteMessage,
  editContact,
};
