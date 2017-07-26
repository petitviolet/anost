import * as React from 'react';
import { UserProps } from '../Container';
import { LoginForm } from '../LoginForm'

export class User extends React.Component<UserProps, {}> {
  render() {
    const {user, token} = this.props.value;
    return (
      <div>
        {(user && token) ? `Hello ${user.name}` : <LoginForm {...this.props}/>}
      </div>
    );
  }
}
