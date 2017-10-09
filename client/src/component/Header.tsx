import * as React from 'react';
import { Logout } from './Logout';
import { UserProps } from '../module/';
import { Link } from 'react-router-dom';
import { Button } from 'react-bootstrap';

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
          : <div className="header-item-component">
            <Link className="header-item" to="/login">
              <Button>Log in</Button>
            </Link>
          </div>
        }
      </div>
    );
  };

const StartPostCreateComponent: React.StatelessComponent<{}> =
  (props: {}) => {
    const path = `/posts/new`;
    return (
      <Link to={path} className="header-item"><Button>New Post</Button></Link>
    );
  }

