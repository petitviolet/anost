import { Dispatch } from 'redux';
import { ReduxAction, ReduxState } from '../store';
import { connect } from 'react-redux';
import { PostListComponent } from '../component';
import { PostListActionDispatcher } from '../action/PostListAction';

export default connect<any, any, { userId: string }>(
  (state: ReduxState, ownProps?: { userId: string}) => {
    const userProps = (state.user.user) ? { userId: state.user.user.id } : ownProps;
    const props = Object.assign({}, state.postList, userProps);
    console.dir(ownProps);
    console.dir(props);
    console.log(ownProps!.userId);
    return { value: props };
    // return { value: state.postList };
  },
  (dispatch: Dispatch<ReduxAction>) => ({ actions: new PostListActionDispatcher(dispatch) })
)(PostListComponent);