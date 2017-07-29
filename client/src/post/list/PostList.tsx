import * as React from 'react';
import { PostListProps, Query } from './Container';
import { Post } from '../model/Post';

export const PostList: React.StatelessComponent<PostListProps> =
  (props: PostListProps) => {
    return (
      <div>
        <QueryBox {...props} />
        {(props.value.loading == true) ? <p> loading... </p> : null}
        {(props.value.error == null) ? null : <p> error! {props.value.error.message} </p>}
        {(props.value.items.length != 0) ? <ol>{
          props.value.items.map((post) => <li><PostItem {...post} /></li>)
        }</ol> : null}
      </div>
    );
  };

const PostItem: React.StatelessComponent<Post> =
  (post: Post) => {
    return (
      <div>
        <a href="#">{post.title}</a>
      </div>
    );
  };

const QueryBox: React.StatelessComponent<PostListProps> =
  (props) => {
    const onQueryChange = (e: any) => {
      props.actions.updateQuery(e.target.value);
    };

    const onClick = (e: any) => {
      e.preventDefault();
      const param = new Query(props.value.query);
      props.actions.list(param);
    };

    return (
      <div>
        <input type="text" placeholder="タイトルで検索" onChange={onQueryChange} required />
        <button onClick={onClick}>Search</button>
      </div>
    )
  };
