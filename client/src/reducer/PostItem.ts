import { PostAction, PostActions } from '../action/PostItemAction';
import { initialPostState, PostState } from '../module/PostItemState';
import { Comment } from '../model';

// reducer endpoint for user
export function reducer(state: PostState = initialPostState, action: PostActions): PostState {
  switch (action.type) {
    case PostAction.REQUEST_START:
      return Object.assign({}, state, { loading: true });
    case PostAction.REQUEST_FINISH:
      return Object.assign({}, state, { loading: false });
    case PostAction.ERROR_OCCURRED:
    case PostAction.ERROR_CLEARD:
      return Object.assign({}, state, { error: action.error });
    case PostAction.SAVE:
    case PostAction.UPDATE:
    case PostAction.SHOW:
      return Object.assign({}, state, { post: action.post });
    case PostAction.ADD_COMMENT:
      const comments: Comment[] = (state.post) ? state.post.comments.concat([action.comment]) : [action.comment];
      const newPost = Object.assign({}, state.post, { comments: comments });
      return Object.assign({}, state, { post: newPost });
    case PostAction.DELETE_COMMENT:
      const deleted = action.comment;
      const cs: Comment[] = (state.post && state.post.comments) ? state.post.comments.filter(c => c.id !== deleted.id) : [];
      const np = Object.assign({}, state.post, { comments: cs });
      return Object.assign({}, state, { post: np });
    default:
      return state;
  }
}
