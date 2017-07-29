import * as React from 'react';
import { UserProps } from './Container';
import { Context } from '../component/Context';

export const LoginForm: React.StatelessComponent<UserProps> =
  (props) => {
    let email: string = '';
    let password: string = '';

    const onEmailChange = (e: any) => {
      email = e.target.value;
    };

    const onPasswordChange = (e: any) => {
      password = e.target.value;
    };

    const onClick = (e: any) => {
      props.actions.logout();
      e.preventDefault();
      props.actions.login(email, password);
    };

    return (
      <div>
        <ul>
          <li>
            <label>Email:
            <input type="text" placeholder="Email Address" onChange={onEmailChange} required />
            </label>
          </li>

          <li>
            <label>Password:
            <input type="password" placeholder="Password" onChange={onPasswordChange} required />
            </label>
          </li>
          <button onClick={onClick}>Sign In</button>
        </ul>
        { // show only if token exists
        }
        {(props.value.loading === true) ? <p> loading... </p> : null}
        <Context {...props} />
      </div>
    );
  };
