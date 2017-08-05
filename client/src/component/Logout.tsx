import * as React from 'react';
import { UserProps } from '../action/UserAction';
import { Link } from 'react-router-dom';

export const Logout: React.StatelessComponent<UserProps> =
  (props: UserProps) => {
    return (
      <Link to="/" onClick={() => props.actions.logout()} id="logout">log out</Link>
    );
  };