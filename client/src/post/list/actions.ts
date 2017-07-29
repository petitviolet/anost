import { Action } from 'redux';
import { Post } from '../model/Post';

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