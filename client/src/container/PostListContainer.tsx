import { Dispatch } from 'redux';
import { ReduxAction, ReduxState } from '../store';
import { connect } from 'react-redux';
import { PostListComponent } from '../component';
import { PostListActionDispatcher } from '../action/PostListAction';

export default connect<any, any, { userId: string }>(
  (state: ReduxState, ownProps?: { userId: string }) => {
    const props = Object.assign({}, state.postList, ownProps);
    return { value: props };
  },
  (dispatch: Dispatch<ReduxAction>) => ({ actions: new PostListActionDispatcher(dispatch) })
)(PostListComponent);