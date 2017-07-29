import { Action } from 'redux';
import { Post } from '../model/Post';

// kinds of action
export enum PostAction {
  REQUEST_START = 'post/request_start',
  REQUEST_FINISH = 'post/request_finish',
  ERROR_OCCURRED = 'post/error_occurred',
  ERROR_CLEARD = 'post/error/cleared',

  SAVE = 'post/save',
  SHOW = 'post/show',
}

interface ChangeRequestStatusAction extends Action {
  type: PostAction.REQUEST_START | PostAction.REQUEST_FINISH;
}
export const startRequestAction = (): ChangeRequestStatusAction => ({
  type: PostAction.REQUEST_START,
});
export const finishRequestAction = (): ChangeRequestStatusAction => ({
  type: PostAction.REQUEST_FINISH,
});

interface SavePostAction extends Action {
  type: PostAction.SAVE;
  post: Post;
}
export const savePostAction = (post: Post): SavePostAction => ({
  type: PostAction.SAVE,
  post: post,
});

interface ShowPostAction extends Action {
  type: PostAction.SHOW;
  post: Post;
}
export const showPostAction = (post: Post): ShowPostAction => ({
  type: PostAction.SHOW,
  post: post,
});

interface ErrorAction extends Action {
  type: PostAction.ERROR_OCCURRED | PostAction.ERROR_CLEARD;
  error: Error | null;
}
export const errorAction = (err: Error): ErrorAction => ({
  type: PostAction.ERROR_OCCURRED,
  error: err,
});
export const clearErrorAction = (): ErrorAction => ({
  type: PostAction.ERROR_CLEARD,
  error: null,
});

export type PostActions = ChangeRequestStatusAction | ErrorAction | SavePostAction | ShowPostAction;