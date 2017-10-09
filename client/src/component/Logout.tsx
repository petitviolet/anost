import * as React from 'react';
import { UserProps } from '../module';
import { Button } from 'react-bootstrap';
import { Link } from 'react-router-dom';

export const Logout: React.StatelessComponent<UserProps> =
  (props: UserProps) => {
    return (
      <Link to="/" onClick={() => props.actions.logout()} className="header-item"><Button>Log out</Button></Link>
    );
  };