import * as React from 'react';
import { UserProps } from '../Container';
import { LoginForm } from '../LoginForm'
import PostList from '../..//post/list/Container';

export class User extends React.Component<UserProps, {}> {
  render() {
    const {user, token} = this.props.value;
    const userIdx = (user) ? user.id : '';
    return (
      <div>
        {(user && token) ? <PostList userId={userIdx} /> : <LoginForm {...this.props}/>}

      </div>
    );
  }
}
