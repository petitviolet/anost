import * as React from 'react';
import { UserProps } from '../action/UserAction';
import { User as UserModel } from '../model/User';
import { LoginForm } from './LoginForm';
import { Link } from 'react-router-dom';

export const User: React.StatelessComponent<UserProps> =
  (props: UserProps) => {
    const { user, token } = props.value;
    if (user && token && props.location.pathname === '/login') { props.history.push('/user/me'); }
    return (
      <div>
        {(user && token) ? <UserDetail user={user} /> : <LoginForm {...props} />}
      </div>
    );
  };

const UserDetail: React.StatelessComponent<{ user: UserModel }> =
  (props: { user: UserModel }) => {
    const user = props.user;
    return (
      <div>
        <table>
          <tbody>
            <tr><td>name</td><td>email</td></tr>
            <tr><td>{user.name}</td>{user.email}</tr>
          </tbody>
        </table>
        <p><Link to="/posts/user/me">post list</Link></p>
      </div>
    );
  };
