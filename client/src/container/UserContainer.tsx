import { Dispatch } from 'redux';
import { ReduxAction, ReduxState } from '../store';
import { connect } from 'react-redux';
import { UserComponent } from '../component';
import { UserActionDispatcher } from '../module/';

export default connect(
  (state: ReduxState) => ({ value: state.user }),
  (dispatch: Dispatch<ReduxAction>) => ({ actions: new UserActionDispatcher(dispatch) })
)(UserComponent);
