import { Dispatch } from 'redux';
import { ReduxAction, ReduxState } from '../store';
import { connect } from 'react-redux';
import { PostComponent } from '../component';
import { PostActionDispatcher } from '../action/PostItemAction';

export default connect<any, any, {postId: string}>(
  (state: ReduxState, ownProps?: { postId: string }) => ({ value: Object.assign({}, state.post, ownProps) }),
  (dispatch: Dispatch<ReduxAction>) => ({ actions: new PostActionDispatcher(dispatch) })
)(PostComponent);
