import * as React from 'react';
import { UserProps } from '../module/';
import { Context } from '../component/Context';
import { Redirect } from 'react-router-dom';

export class LoginForm extends React.Component<UserProps, { email: string, password: string }> {
  constructor(props: UserProps) {
    super(props);
    this.state = { email: '', password: '' };
  }

  private onEmailChange(e: any) {
    this.setState({ email: e.target.value });
  }

  private onPasswordChange(e: any) {
    this.setState({ password: e.target.value });
  }

  private onClick(e: any) {
    console.log('LoginForm', this.props);
    this.props.actions.logout();
    e.preventDefault();
    const { email, password } = this.state;
    if (email && password) {
      this.props.actions.login(email, password);
    } else {
      // for local development
      this.props.actions.login('aa@aa.aa', 'password');
      // this.props.history.push('/posts/user/me');
    }
  }

  render() {
    if (this.props.value.login && this.props.value.login.token) {
      console.log('LoginForm already logged in');
      return <Redirect to="/"/>;
    }
    return (
      <div>
        <ul>
          <li>
            <label>Email:
            <input type="text" placeholder="Email Address" onChange={this.onEmailChange.bind(this)} required />
            </label>
          </li>

          <li>
            <label>Password:
            <input type="password" placeholder="Password" onChange={this.onPasswordChange.bind(this)} required />
            </label>
          </li>
          <button onClick={this.onClick.bind(this)}>Sign In</button>
        </ul>
        <Context {...this.props} />
      </div>
    );
  }
}
