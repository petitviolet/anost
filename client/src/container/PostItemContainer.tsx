import { Dispatch } from 'redux';
import { ReduxAction, ReduxState, withLogin } from '../store';
import { connect } from 'react-redux';
import { PostComponent } from '../component';
import { PostActionDispatcher } from '../module/';

export default connect<any, any, { postId?: string }>(
  (state: ReduxState, ownProps?: { postId?: string }) => {
    const post = (!(ownProps && ownProps.postId) && (!state.post.postId && state.post.post))
      ? Object.assign({}, state.post, { postId: state.post.post.id })
      : state.post;
    const p = Object.assign({}, ownProps, { isEditing: false });
    console.log('state', state);
    return { value: withLogin(state, Object.assign({}, post, p)) };
  },
  (dispatch: Dispatch<ReduxAction>) => ({ actions: new PostActionDispatcher(dispatch) })
)(PostComponent);
