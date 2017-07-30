import * as React from 'react';
import { UserProps } from './Container';
import { Context } from '../component/Context';

export class LoginForm extends React.Component<UserProps, { email: string, password: string }> {
  constructor(props: UserProps) {
    super(props);
    console.dir(this.props);
    this.state = { email: '', password: '' };
  }

  onEmailChange(e: any) {
    this.setState({ email: e.target.value });
  };

  onPasswordChange(e: any) {
    this.setState({ password: e.target.value });
  };

  onClick(e: any) {
    console.dir(this);
    this.props.actions.logout();
    e.preventDefault();
    const { email, password } = this.state;
    if (email && password) {
      this.props.actions.login(email, password);
    } else {
      // for local development
      this.props.actions.login('aa@aa.aa', 'password');
    }
  };

  render() {
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
        { // show only if token exists
        }
        {(this.props.value.loading === true) ? <p> loading... </p> : null}
        <Context {...this.props} />
      </div>
    );
  };
}
