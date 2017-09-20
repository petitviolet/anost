import { Dispatch } from 'redux';
import { ReduxAction, ReduxState, withLogin } from '../store';
import { connect } from 'react-redux';
import { PostListComponent } from '../component';
import { PostListActionDispatcher } from '../module/';

export default connect<any, any, { userId: string }>(
  (state: ReduxState, ownProps?: { userId: string}) => {
    const userProps = (state.user.user) ? { userId: state.user.user.id } : ownProps;
    const props = Object.assign({}, state.postList, userProps);
    return { value: withLogin(state, props) };
  },
  (dispatch: Dispatch<ReduxAction>) => ({ actions: new PostListActionDispatcher(dispatch) })
)(PostListComponent);