import { Action } from 'redux';

enum ActionNames {
  SAVE_TOKEN_NAME = 'user/save_token',
}

interface SaveTokenAction extends Action {
  type: ActionNames.SAVE_TOKEN_NAME;
  newToken: string;
}

export const saveToken = (newToken: string): SaveTokenAction => ({
  type: ActionNames.SAVE_TOKEN_NAME,
  newToken: newToken
});

export interface UserState {
  token: string;
}

export type UserActions = SaveTokenAction;

// with empty token
const initialState: UserState = { token: '' };

export default function reducer(state: UserState = initialState, action: UserActions): UserState {
  switch (action.type) {
    case ActionNames.SAVE_TOKEN_NAME:
      return { token: action.newToken };
    default:
      return state;
  }
}
