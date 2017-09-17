import { PostListState, initialPostListState } from '../module/PostListState';
import { PostListActions, PostListAction } from '../action/PostListAction';

// reducer endpoint for user
export function reducer(state: PostListState = initialPostListState, action: PostListActions): PostListState {
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
