import * as React from 'react';
import { UserProps } from './Container';
import { Link } from 'react-router-dom';

export const Logout: React.StatelessComponent<UserProps> =
  (props: UserProps) => {
    return (
        <div>
          <Link to='/' onClick={() => props.actions.logout()}>log out</Link>
        </div>
    );
  };