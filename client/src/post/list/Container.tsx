import { Dispatch } from 'redux';
import { ReduxAction, ReduxState } from '../../store';
import * as actions from './actions';
import { apiRequest, HttpMethod } from '../../util/request';
import { connect } from 'react-redux';
import { Post } from '../model/Post';
import { PostList as PostListComponent } from './PostList';
import { PostListState } from './state';

enum PostListPath {
  List = '/post',
}

export interface RequestParam {
  isEmpty(): boolean;
  asParam(): string;
}

export class Query implements RequestParam {
  constructor(private readonly title: string) { }
  isEmpty(): boolean {
    return (this.title) ? false : true;
  }
  asParam(): string {
    const t = encodeURIComponent(this.title);
    return `title=${t}`;
  }
}

export class ByUser implements RequestParam {
  private readonly userId: string;
  constructor(userId: string) {
    this.userId = userId;
  }
  isEmpty(): boolean {
    return (this.userId) ? false : true;
  }
  asParam(): string {
    return `user=${this.userId}`;
  }
}

export class PostListActionDispatcher {
  constructor(private dispatch: (action: ReduxAction) => void) { }

  public updateQuery(query: string): void {
    this.dispatch(actions.updateQuery(query));
  }

  public async list(param: RequestParam): Promise<void> {
    const self = this;
    self.dispatch(actions.startRequestAction());
    self.dispatch(actions.clearErrorAction());
    if (param.isEmpty()) {
      self.onError('Query must not be empty.');
      self.dispatch(actions.finishRequestAction());
      return Promise.resolve();
    }
    const path = PostListPath.List + '?' + param.asParam();
    const p = apiRequest(HttpMethod.GET, path)
      .then(res => {
        self.dispatch(actions.finishRequestAction());
        // cast
        const response: Post[] = res.posts;
        // if id exists, request/response are correct.
        if (response != null) {
          console.dir(response);
          self.dispatch(actions.listAction(response));
        } else {
          self.onError('fetch post list failed!');
        }
      })
      .catch(error => {
        console.log('ERROR: ' + error);
        self.onError(error);
      });
    return p;
  }

  private onError(msg: string | Error): void {
    const err: Error = (typeof (msg) === 'string') ? new Error(msg) : msg;
    this.dispatch(actions.errorAction(err));
  }
}

export interface PostListProps {
  value: PostListState;
  actions: PostListActionDispatcher;
}

// update props with userId
export const updatePostListPropsWithUserId = (props: PostListProps, userId?: string): PostListProps => {
  const p = Object.assign({}, props.value, { userId: userId });
  return Object.assign({}, props, { value: p });
};

export default connect<any, any, { userId: string }>(
  (state: ReduxState, ownProps?: { userId: string }) => {
    const p = Object.assign({}, state.postList, ownProps);
    return { value: p };
  },
  (dispatch: Dispatch<ReduxAction>) => ({ actions: new PostListActionDispatcher(dispatch) })
)(PostListComponent);
