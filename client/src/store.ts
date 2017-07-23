import user, { UserActions, UserState } from './user/module';
import { createStore, combineReducers, Action } from 'redux';

export default createStore(
  combineReducers({
    user
  })
);

export type ReduxState = {
  user: UserState
};

export type ReduxAction = UserActions | Action;
