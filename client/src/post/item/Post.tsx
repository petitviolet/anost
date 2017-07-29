import * as React from 'react';
import { PostProps } from './Container';
import { Context } from '../../component/Context';

export const Post: React.StatelessComponent<PostProps> =
  (props: PostProps) => {
    return (
      <div>
        <Context {...props} />
        <p>{ (props.value.post != null) ? JSON.stringify(props.value.post) : null }</p>
      </div>
    );
  };
