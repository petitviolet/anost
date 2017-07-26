import { PostListAction, PostListActions } from './actions';
import { PostListState, initialPostListState } from './state';

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
    default:
      return state;
  }
}
