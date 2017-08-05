import { Header } from '../component';
import { connect } from 'react-redux';
import { Dispatch } from 'redux';
import { ReduxAction, ReduxState } from '../store';
import { UserActionDispatcher } from '../action/UserAction';

export default connect(
  (state: ReduxState) => ({ value: state.user }),
  (dispatch: Dispatch<ReduxAction>) => ({ actions: new UserActionDispatcher(dispatch) })
)(Header);
