import user from './user/module';
import post from './post/item/module';
import postList from './post/list/module';
import { UserState } from './user/state';
import { UserActions } from './user/actions';
import { createStore, combineReducers, Action } from 'redux';
import { PostActions } from './post/item/actions';
import { PostState } from './post/item/state';
import { PostListActions } from './post/list/actions';
import { PostListState } from './post/list/state';
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
  user: UserState;
  post: PostState;
  postList: PostListState;
};

export type ReduxAction = UserActions | PostActions | PostListActions | Action;
