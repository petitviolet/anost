import { Dispatch } from 'redux';
import { ReduxAction, ReduxState, withToken } from '../store';
import { connect } from 'react-redux';
import { PostComponent } from '../component';
import { PostActionDispatcher } from '../module/';

export default connect<any, any, { postId: string }>(
  (state: ReduxState, ownProps?: { postId: string }) => {
    return { value: withToken(state, Object.assign({}, state.post, ownProps)) };
  },
  (dispatch: Dispatch<ReduxAction>) => ({ actions: new PostActionDispatcher(dispatch) })
)(PostComponent);
