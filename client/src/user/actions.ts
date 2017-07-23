import { Action } from 'redux';
// import { User } from './user';

// kinds of action
export enum ActionNames {
  SAVE_TOKEN_NAME = 'user/save_token',
  LOGIN = 'user/login',
}

interface SaveTokenAction extends Action {
  type: ActionNames.SAVE_TOKEN_NAME;
  newToken: string;
}

// factory of save token action
export const saveTokenAction = (newToken: string): SaveTokenAction => ({
  type: ActionNames.SAVE_TOKEN_NAME,
  newToken: newToken
});

interface LoginAction extends Action {
  type: ActionNames.LOGIN;
  newToken: string
}

// factory of login action
export const loginAction = (newToken: string): LoginAction => ({
  type: ActionNames.LOGIN,
  newToken: newToken
});

// all actions for user
export type UserActions = SaveTokenAction | LoginAction;
