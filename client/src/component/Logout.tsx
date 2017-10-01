import * as React from 'react';
import { UserProps } from '../module';
import { Link } from 'react-router-dom';

export const Logout: React.StatelessComponent<UserProps> =
  (props: UserProps) => {
    return (
      <Link to="/" onClick={() => props.actions.logout()} className="header-item">Log out</Link>
    );
  };