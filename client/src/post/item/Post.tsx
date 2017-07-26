import * as React from 'react';
import { PostProps } from './Container';

export const Post: React.StatelessComponent<PostProps> =
  (props: PostProps) => {
    return (
      <div>
        <p>{ (props.value.post != null) ? JSON.stringify(props.value.post) : null }</p>
        {(props.value.loading == true) ? <p> loading... </p> : null}
        {(props.value.error == null) ? null : <p> error! {props.value.error.message} </p>}
      </div>
    );
  };
