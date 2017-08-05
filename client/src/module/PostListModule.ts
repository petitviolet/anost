import { PostListState, initialPostListState } from './PostListState';

import { Action } from 'redux';
import { Post } from '../model';

// kinds of action
export enum PostListAction {
  REQUEST_START = 'post-list/request_start',
  REQUEST_FINISH = 'post-list/request_finish',
  ERROR_OCCURRED = 'post-list/error_occurred',
  ERROR_CLEARD = 'post-list/error/cleared',

  LIST = 'post-list/list',
  UPDATE_QUERY = 'post-list/update_query',
}

interface ChangeRequestStatusAction extends Action {
  type: PostListAction.REQUEST_START | PostListAction.REQUEST_FINISH;
}
export const startRequestAction = (): ChangeRequestStatusAction => ({
  type: PostListAction.REQUEST_START,
});
export const finishRequestAction = (): ChangeRequestStatusAction => ({
  type: PostListAction.REQUEST_FINISH,
});

interface ListAction extends Action {
  type: PostListAction.LIST;
  items: Post[];
}

export const listAction = (items: Post[]): ListAction => ({
  type: PostListAction.LIST,
  items: items,
});

interface UpdateQueryAction extends Action {
  type: PostListAction.UPDATE_QUERY;
  query: string;
}
export const updateQuery = (query: string): UpdateQueryAction => ({
  type: PostListAction.UPDATE_QUERY,
  query: query,
});

interface ErrorAction extends Action {
  type: PostListAction.ERROR_OCCURRED | PostListAction.ERROR_CLEARD;
  error: Error | null;
}
export const errorAction = (err: Error): ErrorAction => ({
  type: PostListAction.ERROR_OCCURRED,
  error: err,
});
export const clearErrorAction = (): ErrorAction => ({
  type: PostListAction.ERROR_CLEARD,
  error: null,
});

export type PostListActions = ChangeRequestStatusAction | ErrorAction | ListAction | UpdateQueryAction;
// reducer endpoint for user
export default function reducer(state: PostListState = initialPostListState, action: PostListActions): PostListState {
  switch (action.type) {
    case PostListAction.REQUEST_START:
      return Object.assign({}, state, { loading: true });
    case PostListAction.REQUEST_FINISH:
      return Object.assign({}, state, { loading: false });
    case PostListAction.ERROR_OCCURRED:
    case PostListAction.ERROR_CLEARD:
      return Object.assign({}, state, { error: action.error });
    case PostListAction.LIST:
      return Object.assign({}, state, { items: action.items });
   case PostListAction.UPDATE_QUERY:
      return Object.assign({}, state, { query: action.query });
    default:
      return state;
  }
}
