import * as React from 'react';
import { PostListProps, Query, ByUser } from './Container';
import { Post } from '../model/Post';
import { Context } from '../../component/Context';

export const PostList: React.StatelessComponent<PostListProps> =
  (props: PostListProps) => {
    // on logged in, fetch and show user's post list.
    const state = props.value;
    if (state.items.length == 0 && !state.loading && !state.query && state.userId) {
      const query = new ByUser(props.value.userId);
      props.actions.list(query);
      console.log("by user!: userId: " + props.value.userId);
    }
    return (
      <div>
        <QueryBox {...props} />
        <Context {...props} />
        {(props.value.items.length !== 0) ? <ol>{
          props.value.items.map((post) => <li key={post.id}><PostItem {...post} /></li>)}</ol> : null}
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
    );
  };
