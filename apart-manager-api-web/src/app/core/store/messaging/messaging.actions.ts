import { createAction, props } from '@ngrx/store';
import { MessagingActionTypes } from './messaging.store';
import { ContactDTO, ScheduledMessageDTO } from '../../../../generated';

const loadContacts = createAction(
  MessagingActionTypes.LOAD_CLIENTS,
  props<{ userId: number }>(),
);

const loadContactsComplete = createAction(
  MessagingActionTypes.LOAD_CLIENTS_COMPLETE,
  props<{ contacts: ContactDTO[] }>(),
);

const loadContactsError = createAction(
  MessagingActionTypes.LOAD_CLIENTS_ERROR,
  props<Error>(),
);

const addOrder = createAction(
  MessagingActionTypes.ADD_ORDER,
  props<{ userId: number; message: ScheduledMessageDTO }>(),
);

const addOrderComplete = createAction(MessagingActionTypes.ADD_ORDER_COMPLETE);

const addOrderError = createAction(
  MessagingActionTypes.ADD_ORDER_ERROR,
  props<Error>(),
);

const MessagingActions = {
  loadContacts,
  loadContactsComplete,
  loadContactsError,
  addOrder,
  addOrderComplete,
  addOrderError,
};

export default MessagingActions;
