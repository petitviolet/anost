import * as React from 'react';
import { Link, Route, Switch } from 'react-router-dom';
import { updatePostListPropsWithUserId, PostListProps, Query, ByUser } from './Container';
import { Post } from '../model/Post';
import { Context } from '../../component/Context';
import { NotFound } from '../../component/NotFound';
import PostItem from '../item/Container';

export const PostList: React.StatelessComponent<PostListProps> =
  (props: PostListProps) => {
    return (
      <div>
        <QueryBox {...props} />
        <Context {...props} />
        <Switch>
          <Route path="/user/me/posts" render={(_) => <PostListComponent {...props} />} />
          <Route path="/user/:userId/posts" render={(param) => {
            console.dir(param);
            return <PostListComponent {...updatePostListPropsWithUserId(props, param.match.params.userId)} />;
          }} />
          <Route path="/post/:postId" render={(param) => {
            console.dir(param);
            const postId: string = param.match.params.postId;
            return <PostItem postId={postId}/>;
          }} />
          <Route component={NotFound} />
        </Switch>
      </div>
    );
  };

const PostListComponent: React.StatelessComponent<PostListProps> =
  (props: PostListProps) => {
    // on logged in, fetch and show user's post list.
    const { items, loading, query, userId } = props.value;
    if (items.length === 0 && !loading && !query && userId) {
      const param = new ByUser(userId);
      props.actions.list(param);
      console.log('by user!: userId: ' + userId);
    }

    return (
      <div>
        {(props.value.items.length !== 0) ? <ol>{
          props.value.items.map((post) =>
            <li key={post.id}>
              <PostListItem {...post} />
            </li>)
        }</ol> : null}
      </div>
    );
  };

const PostListItem: React.StatelessComponent<Post> =
  (post: Post) => {
    const path = `/post/${post.id}`;
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
