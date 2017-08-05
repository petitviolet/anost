import * as React from 'react';
import { PostProps } from '../action/PostItemAction';
import { NotFound } from './NotFound';
import { Post as PostModel } from '../model/Post';

export const Post: React.StatelessComponent<PostProps> =
  (props: PostProps) => {
    return (
      <div>
        {(props.value.post) ?
          <PostItem {...props.value.post} /> :
          <NotFound />}
      </div>
    );
  };

const PostItem: React.StatelessComponent<PostModel> =
  (post: PostModel) => {
    return (
      <div>
        <p>{post.title}[{post.fileType}]</p>
        <code>{post.contents}</code>
      </div>
    );
  };
