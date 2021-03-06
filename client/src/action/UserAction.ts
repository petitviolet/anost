import { Action } from 'redux';
import { User, Token } from '../model';

// kinds of action
export enum UserAction {
  REQUEST_START = 'user/request_start',
  REQUEST_FINISH = 'user/request_finish',
  ERROR_OCCURRED = 'user/error_occurred',
  ERROR_CLEARD = 'user/error/cleared',
  LOGIN = 'user/login',
  LOGOUT = 'user/logout',
}

interface StartRequestAction extends Action {
  type: UserAction.REQUEST_START;
}

export const startRequestAction = (): StartRequestAction => ({
  type: UserAction.REQUEST_START
});

interface FinishRequestAction extends Action {
  type: UserAction.REQUEST_FINISH;
}

export const finishRequestAction = (): FinishRequestAction => ({
  type: UserAction.REQUEST_FINISH
});

interface ErrorAction extends Action {
  type: UserAction.ERROR_OCCURRED | UserAction.ERROR_CLEARD;
  error: Error | null;
}
export const errorAction = (err: Error): ErrorAction => ({
  type: UserAction.ERROR_OCCURRED,
  error: err,
});
export const clearErrorAction = (): ErrorAction => ({
  type: UserAction.ERROR_CLEARD,
  error: null,
});

interface LoginAction extends Action {
  type: UserAction.LOGIN;
  user: User;
  token: Token;
}

// factory of login action
export const loginAction = (user: User, token: Token): LoginAction => ({
  type: UserAction.LOGIN,
  user: user,
  token: token,
});

interface LogoutAction extends Action {
  type: UserAction.LOGOUT;
}

export const logoutAction = (): LogoutAction => ({
  type: UserAction.LOGOUT,
});

// all actions for user
export type UserActions = StartRequestAction | FinishRequestAction | ErrorAction | LoginAction | LogoutAction;
