import * as React from 'react';
import { UserProps } from '../module';
import { User as UserModel } from '../model/';
import { LoginForm } from './LoginForm';
import { Link } from 'react-router-dom';

export const User: React.StatelessComponent<UserProps> =
  (props: UserProps) => {
    const { user, login, location, history } = props.value;
    if (user && login && location && history && location.pathname === '/login') { history.push('/user/me'); }
    return (
      <div>
        {(user && login) ? <UserDetail user={user} /> : <LoginForm {...props} />}
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
        <p><Link to={'/posts/user/' + user.id}>post list</Link></p>
      </div>
    );
  };
