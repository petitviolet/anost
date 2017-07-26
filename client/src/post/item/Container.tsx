import { Dispatch } from 'redux';
import { ReduxAction, ReduxState } from '../../store';
import * as actions from './actions';
import { apiRequest, HttpMethod } from '../../util/request';
import { connect } from 'react-redux';
import { Post } from '../model/Post';
import { Token } from '../../user/model/Token';
import { Post as PostComponent } from './Post'
import { PostState } from './state';

enum PostPath {
  SAVE = '/post',
  SHOW = '/post',
}

export class PostActionDispatcher {
  constructor(private dispatch: (action: ReduxAction) => void) { }

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
    const err: Error = (typeof (msg) == 'string') ? new Error(msg) : msg;
    this.dispatch(actions.errorAction(err));
  }
}

export interface PostProps {
  value: PostState;
  actions: PostActionDispatcher;
}

export default connect(
  (state: ReduxState) => ({ value: state.post }),
  (dispatch: Dispatch<ReduxAction>) => ({ actions: new PostActionDispatcher(dispatch) })
)(PostComponent);
