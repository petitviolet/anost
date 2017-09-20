import * as actions from '../action/PostListAction';
import { apiRequest, HttpMethod } from '../util/request';
import { Post } from '../model';
import { ReduxAction } from '../store';

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

  public updateByUser(userId: string): void {
    this.dispatch(actions.updateByUser(userId));
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
