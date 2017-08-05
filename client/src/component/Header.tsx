import * as React from 'react';
import { UserProps } from '../action/UserAction';
import { Logout } from './Logout';

export const Header: React.StatelessComponent<UserProps> =
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
