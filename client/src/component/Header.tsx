import * as React from 'react';
import { UserProps, UserActionDispatcher } from '../user/Container';
import { Logout } from '../user/Logout';
import { connect } from 'react-redux';
import { Dispatch } from 'redux';
import { ReduxAction, ReduxState } from '../store';

const Header: React.StatelessComponent<UserProps> =
  // const user = this.props.value.user;
  (props) => {
    return (
      <div>
        Anost!
        {(props.value.user && props.value.token) ? `   hello ${props.value.user.name}` : null}
        {(props.value.user && props.value.token) ? <Logout {...props} /> : null}
      </div>
    );
  };

export default connect(
  (state: ReduxState) => ({ value: state.user }),
  (dispatch: Dispatch<ReduxAction>) => ({ actions: new UserActionDispatcher(dispatch) })
)(Header);
