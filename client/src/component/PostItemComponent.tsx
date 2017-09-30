import * as React from 'react';
import { PostProps } from '../module';
import { Post as PostModel } from '../model/Post';
import { Context } from './Context';
import { NotFound } from './NotFound';
import { Link } from 'react-router-dom';
import { createEditor, onEditorChange } from './CodeEditor';
import { Comments as CommentsComponent } from './CommentComponent';
import { locationPush } from '../util';

// state of PostComponent
interface PostComponentState {
  isEditing: boolean;
}

class PostEdit {
  readonly id: string;
  title: string;
  fileType: string;
  contents: string;
  constructor(id: string, title: string, fileType: string, contents: string) {
    this.id = id;
    this.title = title;
    this.fileType = fileType;
    this.contents = contents;
  }
}
const fromModel = (post: PostModel | null): PostEdit => {
  if (post) {
    return new PostEdit(post.id, post.title, post.fileType, post.contents);
  } else {
    return new PostEdit('', '', '', '');
  }
};

export class Post extends React.Component<PostProps, PostComponentState> {
  constructor(props: PostProps) {
    super(props);
    this.state = { isEditing: props.value.isEditing };
  }

  startEdit = (e: any) => {
    this.setState({ isEditing: true });
  }

  submitEdit = (e: any, editedPost: PostEdit) => {
    this.setState({ isEditing: false });

    const { id, title, fileType, contents } = editedPost;
    const login = this.props.value.login;
    if (login && id) {
      this.props.actions.update(id, title, fileType, contents, login.token);
    } else if (login && !id) {
      this.props.actions.save(title, fileType, contents, login.token);
    } else {
      this.props.actions.onError('User not logged in.');
    }
  }

  cancelEdit = (e: any) => {
    this.setState({ isEditing: false });
  }

  render() {
    const { value: props, actions: actions } = this.props;
    if (!props.error && !props.loading) {
      if (props.match && (!props.post || props.post.id !== props.match.params.id)) {
        const id: string = props.match.params.id;
        actions.show(id);
      } else if (props.post && !props.postId) {
        console.log('push', props);
        locationPush(`/posts/${props.post.id}`);
      } else {
        console.log(props)
      }
    }
    // console.log('state', this.state);
    // console.log('props', this.props);
    return (
      <div>
        <Context {...this.props} />
        {(this.state.isEditing)
          ? <PostItemEdit {...{ post: props.post, submitEdit: this.submitEdit, cancelEdit: this.cancelEdit }} />
          : (props.post)
            ? <PostItem {...{ postProps: this.props, startEdit: this.startEdit }} />
            : (props.loading || props.error) ? null : <NotFound />
        }
      </div>
    );
  }
}

// a component for editing Post
interface PostItemEditProps {
  post: PostModel | null;
  submitEdit: any;
  cancelEdit: any;
}
interface PostItemEditState {
  postEdit: PostEdit;
  editor: any;
}

export class PostItemEdit extends React.Component<PostItemEditProps, PostItemEditState> {
  constructor(props: { post: PostModel, submitEdit: any, cancelEdit: any }) {
    super(props);
    this.state = { postEdit: fromModel(props.post), editor: null };
  }

  onTitleChange = (e: any) => {
    const edit = this.state.postEdit;
    edit.title = e.target.value;
    this.setState({ postEdit: edit });
  }
  onFileTypeChange = (e: any) => {
    const edit = this.state.postEdit;
    edit.fileType = e.target.value;
    this.setState({ postEdit: edit });
  }
  onContentsChange = (value: string, event?: any) => {
    const newPostEdit = this.state.postEdit;
    newPostEdit.contents = value;
    this.setState({ postEdit: newPostEdit });
  }

  render() {
    const { submitEdit, cancelEdit } = this.props;
    const post = this.state.postEdit;
    const inputStyle = {
      fontSize: '18px', paddingBottom: '5px', margin: '8px',
      border: 'none', borderBottom: 'solid 2px blue'
    };
    return (
      <div className="App-Width">
        <input type="text" placeholder="file name" style={inputStyle}
          onChange={this.onTitleChange} value={post.title} />
        <input type="text" placeholder="file type" style={inputStyle}
          onChange={this.onFileTypeChange} value={post.fileType} />
        <PostItemEditor {...{ post: post, onChange: this.onContentsChange }} />
        <div style={{ color: 'blue', padding: '3px' }}>
          <Link to="#" onClick={(e) => {
            submitEdit(e, this.state.postEdit);
          }}>Submit</Link></div>
        <div style={{ color: 'red', padding: '3px' }}><Link to="#" onClick={(e) => cancelEdit(e)}>Cancel</Link></div>
      </div>
    );
  }
}

const PostItemEditor: React.StatelessComponent<{ post: PostEdit, onChange: onEditorChange }> =
  (props: { post: PostEdit, onChange: onEditorChange }) => {
    return createEditor(props.post, false, props.onChange);
  };

const PostItem: React.StatelessComponent<{ postProps: PostProps, startEdit: any }> =
  (props: { postProps: PostProps, startEdit: any }) => {
    const { postProps, startEdit } = props;
    if (!postProps.value.post) return null;
    const post = postProps.value.post;
    return (
      <div>
        <p>{post.title}[{post.fileType}]</p>
        <Link to="#" onClick={startEdit}>Edit</Link>
        <PostItemViewer {...post} />
        <CommentsComponent {...postProps} />
      </div>
    );
  };

const PostItemViewer: React.StatelessComponent<PostModel> =
  (post: PostModel) => {
    return createEditor(post, true);
  };
