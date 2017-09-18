import * as actions from '../action/PostItemAction';
import { apiRequest, HttpMethod } from '../util/request';
import { Token, Post, Comment } from '../model';
import { PostState } from '../module/PostItemState';
import { ReduxAction } from '../store';

enum PostPath {
  SAVE = '/post',
  UPDATE = '/post',
  SHOW = '/post',
  COMMENT = '/comment',
}

export class PostActionDispatcher {
  constructor(private dispatch: (action: ReduxAction) => void) { }

  public async show(postId: string): Promise<void> {
    const self = this;
    self.dispatch(actions.startRequestAction());
    self.dispatch(actions.clearErrorAction());
    if (!postId) {
      self.onError('PostId must not be empty.');
      self.dispatch(actions.finishRequestAction());
      return Promise.resolve();
    }
    const path = `${PostPath.SHOW}/${postId}`;
    const p = apiRequest(HttpMethod.GET, path)
      .then(res => {
        // cast
        const post: Post = res;
        // if id exists, request/response are correct.
        if (post && post.id) {
          self.dispatch(actions.showPostAction(post));
        } else {
          self.onError(`fetch post(${postId}) failed!`);
        }
        self.dispatch(actions.finishRequestAction());
      })
      .catch(error => {
        console.log('ERROR: ' + error);
        self.onError(error);
      });
    return p;
  }

  public async update(id: string, title: string, fileType: string, contents: string, token: Token): Promise<void> {
    const self = this;
    self.dispatch(actions.clearErrorAction());
    if (!id || !title || !fileType || !contents) {
      console.log(`id: ${id}, title: ${title}, fileType: ${fileType}, contents: ${contents}`);
      self.onError('Id, Title, FileType, Contents must not be empty.');
      return Promise.resolve();
    }
    const body = { id: id, title: title, fileType: fileType, contents: contents };

    self.dispatch(actions.startRequestAction());
    const p = apiRequest(HttpMethod.PUT, PostPath.UPDATE, token.value, body)
      .then(res => {
        self.dispatch(actions.finishRequestAction());
        // cast
        const post: Post = res;
        // if id exists, request/response are correct.
        console.log(`post update: ${post}`);
        if (post && post.id) {
          console.dir(post);
          self.dispatch(actions.updatePostAction(post));
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

  public async save(title: string, fileType: string, contents: string, token: Token): Promise<void> {
    const self = this;
    self.dispatch(actions.clearErrorAction());
    if (!title || !fileType || !contents) {
      console.log(`title: ${title}, fileType: ${fileType}, contents: ${contents}`);
      self.onError('Title, FileType, Contents must not be empty.');
      return Promise.resolve();
    }
    const body = { title: title, fileType: fileType, contents: contents };

    self.dispatch(actions.startRequestAction());
    const p = apiRequest(HttpMethod.POST, PostPath.SAVE, token.value, body)
      .then(res => {
        self.dispatch(actions.finishRequestAction());
        // cast
        const post: Post = res;
        // if id exists, request/response are correct.
        console.log(`post save: ${post}`);
        if (post && post.id) {
          console.dir(post);
          self.dispatch(actions.savePostAction(post));
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

  public async addComment(post: Post, sentence: string, token: Token): Promise<void> {
    const self = this;
    self.dispatch(actions.clearErrorAction());
    if (!sentence) {
      self.onError('sentence must not be empty.');
      return Promise.resolve();
    }
    const body = { postId: post.id, sentence: sentence };

    self.dispatch(actions.startRequestAction());
    const p = apiRequest(HttpMethod.POST, PostPath.COMMENT, token.value, body)
      .then(res => {
        self.dispatch(actions.finishRequestAction());
        // cast
        const comment: Comment = res;
        // if id exists, request/response are correct.
        console.log(`commented: ${comment}`);
        if (comment && comment.id) {
          console.dir(comment);
          self.dispatch(actions.addCommentAction(comment));
        } else {
          self.onError('comment failed!');
        }
      })
      .catch(error => {
        console.log('ERROR: ' + error);
        self.onError(error);
      });
    return p;
  }


  public onError(msg: string | Error): void {
    const err: Error = (typeof (msg) === 'string') ? new Error(msg) : msg;
    this.dispatch(actions.errorAction(err));
  }
}

export interface PostProps {
  value: PostState;
  actions: PostActionDispatcher;
}
