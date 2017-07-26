import { Action } from 'redux';
import { User } from './user';

// kinds of action
export enum UserAction {
  REQUEST_START = "user/request_start",
  LOGIN = 'user/login',
}

interface StartRequestAction extends Action {
  type: UserAction.REQUEST_START;
}

export const startRequestAction = (): StartRequestAction => ({
  type: UserAction.REQUEST_START
})

interface LoginAction extends Action {
  type: UserAction.LOGIN;
  user: User
}

// factory of login action
export const loginAction = (user: User): LoginAction => ({
  type: UserAction.LOGIN,
  user: user,
});

// all actions for user
export type UserActions = StartRequestAction | LoginAction;
