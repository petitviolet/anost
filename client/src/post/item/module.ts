import { PostAction, PostActions } from './actions';
import { PostState, initialPostState } from './state';

// reducer endpoint for user
export default function reducer(state: PostState = initialPostState, action: PostActions): PostState {
  switch (action.type) {
    case PostAction.REQUEST_START:
      return Object.assign({}, state, { loading: true });
    case PostAction.REQUEST_FINISH:
      return Object.assign({}, state, { loading: false });
    case PostAction.ERROR_OCCURRED:
    case PostAction.ERROR_CLEARD:
      return Object.assign({}, state, { error: action.error });
    case PostAction.SAVE:
      return Object.assign({}, state, { post: action.post });
    case PostAction.SHOW:
      return Object.assign({}, state, { post: action.post });
    default:
      return state;
  }
}
