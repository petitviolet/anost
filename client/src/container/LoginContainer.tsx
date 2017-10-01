import { Dispatch } from 'redux';
import { ReduxAction, ReduxState } from '../store';
import { connect } from 'react-redux';
import { UserActionDispatcher } from '../module/';
import { LoginForm } from '../component';

export default connect(
  (state: ReduxState) => ({ value: state.user }),
  (dispatch: Dispatch<ReduxAction>) => ({ actions: new UserActionDispatcher(dispatch) })
)(LoginForm);
