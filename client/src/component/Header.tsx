import * as React from 'react';
import { Logout } from './Logout';
import { UserProps } from '../module/';
import { Link } from 'react-router-dom';

export const Header: React.StatelessComponent<UserProps> =
  (props) => {
    console.log('header', props);
    return (
      <div>
        Anost!
        {(props.value.user && props.value.login)
          ? <Logout {...props} />
          : <Link style={loginLinkStyle} to="/login">Login</Link>}
      </div>
    );
  };

 const loginLinkStyle = {
   float: 'right',
   marginRight: '1em',
   color: 'white',
 }
