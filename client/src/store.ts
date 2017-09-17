import { user, post, postList } from './reducer';
import * as a from './action';
import * as m from './module';
import { createStore, combineReducers, Action } from 'redux';
import { routerReducer } from 'react-router-redux';
import { Token } from './model/Token';

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

export const withToken = (s: ReduxState, props: any) => {
  const token: Token | undefined = s.user.token;
  return Object.assign({}, props, { token: token });
}

export type ReduxAction = a.UserActions | a.PostActions | a.PostListActions | Action;
