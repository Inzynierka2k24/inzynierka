import { createAction, props } from '@ngrx/store';
import { MessagingActionTypes } from './messaging.store';
import {
  Contact,
  ContactDTO,
  ScheduledMessageDTO,
} from '../../../../generated';

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
  props<{ userId: number; contactId: number; message: ScheduledMessageDTO }>(),
);

const addOrderComplete = createAction(
  MessagingActionTypes.ADD_ORDER_COMPLETE,
  props<{ contactId: number; message: ScheduledMessageDTO }>(),
);

const addOrderError = createAction(
  MessagingActionTypes.ADD_ORDER_ERROR,
  props<Error>(),
);

const addContact = createAction(
  MessagingActionTypes.ADD_CONTACT,
  props<{ userId: number; contact: Contact }>(),
);

const addContactComplete = createAction(
  MessagingActionTypes.ADD_CONTACT_COMPLETE,
  props<{ contact: Contact }>(),
);

const addContactError = createAction(
  MessagingActionTypes.ADD_CONTACT_ERROR,
  props<Error>(),
);

const deleteMessage = createAction(
  MessagingActionTypes.DELETE_MESSAGE,
  props<{ userId: number; contactId: number; messageId: number }>(),
);

const deleteMessageComplete = createAction(
  MessagingActionTypes.DELETE_MESSAGE_COMPLETE,
  props<{ contactId: number; messageId: number }>(),
);

const deleteMessageError = createAction(
  MessagingActionTypes.DELETE_MESSAGE_ERROR,
  props<Error>(),
);

const deleteContact = createAction(
  MessagingActionTypes.DELETE_CONTACT,
  props<{ userId: number; contactId: number }>(),
);

const deleteContactComplete = createAction(
  MessagingActionTypes.DELETE_CONTACT_COMPLETE,
  props<{ contactId: number }>(),
);

const deleteContactError = createAction(
  MessagingActionTypes.DELETE_CONTACT_ERROR,
  props<Error>(),
);

const editContact = createAction(
  MessagingActionTypes.EDIT_CONTACT,
  props<{ userId: number; contact: Contact }>(),
);

const editContactComplete = createAction(
  MessagingActionTypes.EDIT_CONTACT_COMPLETE,
  props<{ contact: Contact }>(),
);

const editContactError = createAction(
  MessagingActionTypes.EDIT_CONTACT_ERROR,
  props<Error>(),
);

const MessagingActions = {
  loadContacts,
  loadContactsComplete,
  loadContactsError,
  addOrder,
  addOrderComplete,
  addOrderError,
  deleteMessage,
  deleteMessageComplete,
  deleteMessageError,
  addContact,
  addContactComplete,
  addContactError,
  deleteContact,
  deleteContactComplete,
  deleteContactError,
  editContact,
  editContactComplete,
  editContactError,
};

export default MessagingActions;
