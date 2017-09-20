import { user, post, postList } from './reducer';
import * as a from './action';
import * as m from './module';
import { createStore, combineReducers, Action } from 'redux';
import { routerReducer } from 'react-router-redux';

export default createStore(
  combineReducers({
    user,
    post,
    postList,
    routing: routerReducer,
  })
);

export type ReduxState = {
  user: m.UserState;
  post: m.PostState;
  postList: m.PostListState;
};

export const withLogin = (s: ReduxState, props: any) => {
  const login = s.user.login;
  return Object.assign({}, props, { login: login });
};

export type ReduxAction = a.UserActions | a.PostActions | a.PostListActions | Action;
