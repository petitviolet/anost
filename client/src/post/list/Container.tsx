import { Dispatch } from 'redux';
import { ReduxAction, ReduxState } from '../../store';
import * as actions from './actions';
import { apiRequest, HttpMethod } from '../../util/request';
import { connect } from 'react-redux';
import { Post } from '../model/Post';
import { PostList as PostListComponent } from './PostList'
import { PostListState } from './state';
// import { LoginForm } from './LoginForm';

enum PostListPath {
  List = '/post',
}

export interface RequestParam {
  isEmpty(): boolean;
  asParam(): string;
}

export class Query implements RequestParam {
  constructor(private readonly title: string) {}
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

  public async list(param: RequestParam): Promise<void> {
    const self = this;
    self.dispatch(actions.clearErrorAction());
    if (param.isEmpty()) {
      self.onError('Query must not be empty.');
      return Promise.resolve();
    }
    const path = PostListPath.List + '?' + param.asParam();
    self.dispatch(actions.startRequestAction());
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
    const err: Error = (typeof (msg) == 'string') ? new Error(msg) : msg;
    this.dispatch(actions.errorAction(err));
  }
}

export interface PostListProps {
  value: PostListState;
  actions: PostListActionDispatcher;
}

export default connect(
  (state: ReduxState) => ({ value: state.postList }),
  (dispatch: Dispatch<ReduxAction>) => ({ actions: new PostListActionDispatcher(dispatch) })
)(PostListComponent);
