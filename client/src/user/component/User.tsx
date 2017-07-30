import * as React from 'react';
import { UserProps } from '../Container';
import { User as UserModel } from '../model/User';
import { LoginForm } from '../LoginForm';

export class UserComponent extends React.Component<UserProps, {}> {
  render() {
    const { user, token } = this.props.value;
        // {(user && token) ? <UserDetail user={user} /> : <Redirect to="/login" />}
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
      <table>
        <tbody>
        <tr><td>name</td><td>email</td></tr>
        <tr><td>{user.name}</td>{user.email}</tr>
        </tbody>
      </table>
    );
  }
}
