import { user, post, postList } from './reducer';
import * as a from './action';
import * as m from './module';
import { createStore, combineReducers, Action, applyMiddleware } from 'redux';
import { routerReducer, routerMiddleware } from 'react-router-redux';
import createBrowserHistory from 'history/createBrowserHistory';

export const history = createBrowserHistory();
const middleware = routerMiddleware(history);

export default createStore(
  combineReducers({
    user,
    post,
    postList,
    router: routerReducer,
  }),
  applyMiddleware(middleware)
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
