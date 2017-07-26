import { Action } from 'redux';
import { User } from './user';

// kinds of action
export enum UserAction {
  LOGIN = 'user/login',
}

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
export type UserActions = LoginAction;
