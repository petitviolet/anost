import * as React from 'react';
import { UserProps } from '../user/Container';
import { Dispatch } from 'redux';
import { ReduxAction, ReduxState } from '../store';
import { connect } from 'react-redux';

export class Header extends React.Component<UserProps, {}> {
  render() {
    // const user = this.props.value.user;
    return (
      <div>
        <p>Anost!
        </p>
      </div>
    );
  }
}

export default connect(
  (state: ReduxState) => ({ value: state.user }),
  (dispatch: Dispatch<ReduxAction>) => ({}),
)(Header);
