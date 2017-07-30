import * as React from 'react';
import { UserProps } from '../Container';
import { User as UserModel } from '../model/User';
import { LoginForm } from '../LoginForm';
import { Link } from 'react-router-dom';

export class UserComponent extends React.Component<UserProps, {}> {
  render() {
    const { user, token } = this.props.value;
    if (user && token && this.props.location.pathname === '/login') this.props.history.push('/user/me');
    return (
      <div>
        {(user && token) ? <UserDetail user={user} /> : <LoginForm {...this.props}/>}
      </div>
    );
  }
}

class UserDetail extends React.Component<{ user: UserModel }> {
  render() {
    const user = this.props.user;
    return (
      <div>
      <table>
        <tbody>
        <tr><td>name</td><td>email</td></tr>
        <tr><td>{user.name}</td>{user.email}</tr>
        </tbody>
      </table>
      <p><Link to="/user/me/posts">post list</Link></p>
      </div>
    );
  }
}
