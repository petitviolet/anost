import * as React from 'react';
import { Logout } from './Logout';
import { UserProps } from '../module/';
import { Link } from 'react-router-dom';

export const Header: React.StatelessComponent<UserProps> =
  (props) => {
    console.log('header', props);
    return (
      <div>
        Anost!
        {(props.value.user && props.value.login)
          ? <div className="header-item-component">
              <StartPostCreateComponent /><Logout {...props} />
            </div>
          : <div className="header-item-component"><Link className="header-item" to="/login">Login</Link></div>
        }
      </div>
    );
  };

const StartPostCreateComponent: React.StatelessComponent<{}> =
  (props: {}) => {
    const path = `/posts/new`;
    return (
      <Link to={path} className="header-item">New Post</Link>
    );
  }

