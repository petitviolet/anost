import user from './user/module';
import { UserState } from './user/state';
import { UserActions } from './user/actions';
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
