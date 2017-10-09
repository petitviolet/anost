import { Dispatch } from 'redux';
import { ReduxAction, ReduxState, withLogin } from '../store';
import { connect } from 'react-redux';
import { PostComponent } from '../component';
import { PostActionDispatcher } from '../module/';

export default connect(
  (state: ReduxState) => {
    console.log('state', state);
    console.log('state', state.post);
    if (state.post.post && !state.post.postId) {
      // after created post
      return { value: withLogin(state, Object.assign({}, state.post, { post: state.post.post, postId: state.post.post.id, isEditing: false })) };
    } else {
      // first
      return { value: withLogin(state, Object.assign({}, state.post, { post: null, postId: '', isEditing: true })) };
    }
  },
  (dispatch: Dispatch<ReduxAction>) => ({ actions: new PostActionDispatcher(dispatch) })
)(PostComponent);
