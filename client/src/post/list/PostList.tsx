import * as React from 'react';
import { PostListProps } from './Container';

export const PostList: React.StatelessComponent<PostListProps> =
  (props: PostListProps) => {
    return (
      <div>
        <QueryBox {...props} />
        {(props.value.loading == true) ? <p> loading... </p> : null}
        {(props.value.error == null) ? null : <p> error! {props.value.error.message} </p>}
        {(props.value.items.length != 0) ? <p>{JSON.stringify(props.value.items)}</p> : null}
      </div>
    );
  };

const QueryBox: React.StatelessComponent<PostListProps> =
  (props) => {
    let query: string = '';
    const onQueryChange = (e: any) => {
      query = e.target.value;
    };

    const onClick = (e: any) => {
      e.preventDefault();
      props.actions.list({ query: query, user: '' });
    };

    return (
      <div>
        <input type="text" placeholder="タイトルで検索" onChange={onQueryChange} required />
        <button onClick={onClick}>Search</button>
      </div>
    )
  };
