import { Action } from 'redux';
import { Post, Comment } from '../model';

// kinds of action
export enum PostAction {
  REQUEST_START = 'post/request_start',
  REQUEST_FINISH = 'post/request_finish',
  ERROR_OCCURRED = 'post/error_occurred',
  ERROR_CLEARD = 'post/error/cleared',

  SAVE = 'post/save',
  SHOW = 'post/show',
  UPDATE = 'post/update',
  ADD_COMMENT = 'post/comment/add',
  DELETE_COMMENT = 'post/comment/delete',
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

interface UpdatePostAction extends Action {
  type: PostAction.UPDATE;
  post: Post;
}
export const updatePostAction = (post: Post): UpdatePostAction => ({
  type: PostAction.UPDATE,
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

interface AddCommentAction extends Action {
  type: PostAction.ADD_COMMENT;
  comment: Comment;
}
export const addCommentAction = (comment: Comment): AddCommentAction => ({
  type: PostAction.ADD_COMMENT,
  comment: comment,
});

interface DeleteCommentAction extends Action {
  type: PostAction.DELETE_COMMENT;
  comment: Comment;
}
export const deleteCommentAction = (comment: Comment): DeleteCommentAction => ({
  type: PostAction.DELETE_COMMENT,
  comment: comment,
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

export type PostActions = ChangeRequestStatusAction | ErrorAction |
            SavePostAction | ShowPostAction | UpdatePostAction |
            AddCommentAction | DeleteCommentAction ;