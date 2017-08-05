import * as actions from '../module/PostItemModule';
import { apiRequest, HttpMethod } from '../util/request';
import { Post } from '../model/Post';
import { Token } from '../model';
import { PostState } from '../module/PostItemState';
import { ReduxAction } from '../store';

enum PostPath {
  SAVE = '/post',
  SHOW = '/post',
}

export class PostActionDispatcher {
  constructor(private dispatch: (action: ReduxAction) => void) { }

  public async show(postId: string): Promise<void> {
    const self = this;
    self.dispatch(actions.clearErrorAction());
    if (!postId) {
      self.onError('PostId must not be empty.');
      return Promise.resolve();
    }
    self.dispatch(actions.startRequestAction());
    const path = PostPath.SHOW + postId;
    const p = apiRequest(HttpMethod.GET, path)
      .then(res => {
        self.dispatch(actions.finishRequestAction());
        // cast
        const post: Post = res.post;
        // if id exists, request/response are correct.
        if (!post) {
          console.dir(post);
          self.dispatch(actions.showPostAction(post));
        } else {
          self.onError(`fetch post(${postId}) failed!`);
        }
      })
      .catch(error => {
        console.log('ERROR: ' + error);
        self.onError(error);
      });
    return p;
  }

  public async save(title: string, fileType: string, contents: string, token: Token): Promise<void> {
    const self = this;
    self.dispatch(actions.clearErrorAction());
    if (!title || !fileType || !contents) {
      self.onError('Title, FileType, Contents must not be empty.');
      return Promise.resolve();
    }
    const body = { title: title, file_type: fileType, contents: contents };

    self.dispatch(actions.startRequestAction());
    const p = apiRequest(HttpMethod.POST, PostPath.SAVE, token.value, body)
      .then(res => {
        self.dispatch(actions.finishRequestAction());
        // cast
        const response: Post = res;
        // if id exists, request/response are correct.
        if (response != null) {
          console.dir(response);
          self.dispatch(actions.savePostAction(response));
        } else {
          self.onError('Save post failed!');
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

export interface PostProps {
  value: PostState;
  actions: PostActionDispatcher;
}
