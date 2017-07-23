import { Dispatch } from 'redux';
import { ReduxAction, ReduxState } from '../store';
import { saveToken } from './module';
import { connect } from 'react-redux';
import { User } from './User';

export class UserActionDispatcher {
  constructor(private dispatch: (action: ReduxAction) => void) {}

  public saveToken(token: string) {
    this.dispatch(saveToken(token));
  }
}

export default connect(
  (state: ReduxState) => ({ value: state.user }),
  (dispatch: Dispatch<ReduxAction>) => ({actions: new UserActionDispatcher(dispatch)})
)(User);
