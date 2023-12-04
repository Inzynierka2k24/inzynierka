import { createReducer, on } from '@ngrx/store';
import { initialState } from './messaging.store';
import MessagingActions from './messaging.actions';
import { ContactDTO } from '../../../../generated';

export const messagingReducer = createReducer(
  initialState,
  on(
    MessagingActions.loadContacts,
    MessagingActions.addOrder,
    MessagingActions.deleteMessage,
    MessagingActions.addContact,
    MessagingActions.deleteContact,
    MessagingActions.editContact,
    (state) => ({
      ...state,
      loading: true,
    }),
  ),
  on(MessagingActions.loadContactsComplete, (state, action) => ({
    ...state,
    loading: false,
    contacts: action.contacts,
  })),
  on(MessagingActions.addOrderComplete, (state, action) => {
    const contacts =
      state.contacts?.map((contact) => {
        if (contact.id === action.contactId) {
          contact.messages.push(action.message);
        }
        return contact;
      }) ?? [];
    return {
      ...state,
      loading: false,
      contacts,
    };
  }),
  on(MessagingActions.addContact, (state, action) => {
    const contactDTO: ContactDTO = {
      ...action.contact,
      messages: [],
      apartments: [],
      notificationSettings: {
        emailNotifications: false,
        smsNotifications: false,
      },
    };
    return {
      ...state,
      loading: false,
      contacts: [...(state.contacts ?? []), contactDTO],
    };
  }),
  on(MessagingActions.deleteMessageComplete, (state, action) => ({
    ...state,
    loading: false,
    contacts:
      state.contacts?.map((contact) => {
        if (contact.id === action.contactId) {
          contact.messages = contact.messages.filter(
            (message) => message.id !== action.messageId,
          );
        }
        return contact;
      }) ?? [],
  })),
  on(MessagingActions.deleteContactComplete, (state, action) => ({
    ...state,
    loading: false,
    contacts: state.contacts?.filter(
      (contact) => contact.id !== action.contactId,
    ),
  })),
  on(MessagingActions.editContactComplete, (state, action) => ({
    ...state,
    loading: false,
    contacts:
      state.contacts?.map((contact) => {
        if (contact.id === action.contact.id) {
          return { ...contact, ...action.contact };
        }
        return contact;
      }) ?? [],
  })),
  on(
    MessagingActions.loadContactsError,
    MessagingActions.addOrderError,
    MessagingActions.deleteMessageError,
    MessagingActions.addContactError,
    MessagingActions.deleteContactError,
    MessagingActions.editContactError,
    (state, action) => ({
      ...state,
      loading: false,
      error: action,
    }),
  ),
);
