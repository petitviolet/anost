import user from './module/UserModule';
import post from './module/PostItemModule';
import postList from './module/PostListModule';
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

export type ReduxAction = m.UserActions | m.PostActions | m.PostListActions | Action;
