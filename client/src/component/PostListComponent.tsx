import * as React from 'react';
import { Link, Route, Switch } from 'react-router-dom';
import { Query, ByUser, PostListActionDispatcher } from '../module';
import { Post } from '../model/Post';
import { Context } from './Context';
import { NotFound } from './NotFound';

import { PostListState } from '../module';

export interface PostListProps {
  value: PostListState;
  actions: PostListActionDispatcher;
}

// update props with userId
export const updatePostListPropsWithUserId = (props: PostListProps, userId?: string): PostListProps => {
  const p = Object.assign({}, props.value, { userId: userId });
  return Object.assign({}, props, { value: p });
};
export const PostList: React.StatelessComponent<PostListProps> =
  (props: PostListProps) => {
    return (
      <div>
        <QueryBox {...props} />
        <Context {...props} />
        <StartPostCreateComponent />
        <Switch>
          <Route exact path="/posts/user/me" render={(param) => {
            console.log('/posts/user/me');
            console.dir(param);
            return <PostListComponent {...props} />;
          }} />
          <Route path="/posts/user/:userId" render={(param) => {
            const userId = param.match.params.userId;
            console.log(`/posts/user/${userId}`);
            if (props.value.userId && props.value.userId !== userId) {
              props.actions.updateByUser(userId);
              return null;
            }
            return <PostListComponent {...props} />;
          }} />
          <Route component={NotFound} />
        </Switch>
      </div>
    );
  };

const StartPostCreateComponent: React.StatelessComponent<{}> =
  (props: {}) => {
    const path = `/posts/new`;
    return (
      <div>
        <Link to={path}>NEW POST</Link>
      </div>
    );
  }

const PostListComponent: React.StatelessComponent<PostListProps> =
  (props: PostListProps) => {
    // on logged in, fetch and show user's post list.
    const { items, loading, query, userId } = props.value;
    if (!items && !loading && !query && userId) {
      const param = new ByUser(userId);
      props.actions.list(param);
      console.log('by user!: userId: ' + userId);
      return <div></div>;
    }
    console.log('PostListComponent');
    console.dir(props.value);

    return (
      <div>
        {(props.value.items && props.value.items.length !== 0) ? <ul>{
          props.value.items.map((post) =>
            <li key={post.id}>
              <PostListItem {...post} />
            </li>)
        }</ul> : <div>no posts.</div>}
      </div>
    );
  };

const PostListItem: React.StatelessComponent<Post> =
  (post: Post) => {
    const path = `/posts/${post.id}`;
    return (
      <div>
        <Link to={path}>{post.title}</Link>
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
