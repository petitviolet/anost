import * as React from 'react';
import { PostProps } from '../action/PostItemAction';
import { Post as PostModel } from '../model/Post';
import { Context } from './Context';
import { NotFound } from './NotFound';

export const Post: React.StatelessComponent<PostProps> =
  (props: PostProps) => {
    if (!props.value.error && !props.value.loading && !props.value.post && props.value.match) {
      const id: string = props.value.match.params.id;
      console.log('from route! id: ' + id);
      console.dir(props.value);
      props.actions.show(id);
    }
    return (
      <div>
        <Context {...props} />
        {(props.value.post) ? <PostItem {...props.value.post!} /> : <NotFound />}
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
