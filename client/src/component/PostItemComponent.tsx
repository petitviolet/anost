import * as React from 'react';
import { PostProps } from '../action/PostItemAction';
import { Post as PostModel } from '../model/Post';
import { Context } from './Context';
import { NotFound } from './NotFound';
import AceEditor from 'react-ace';
import { Link } from 'react-router-dom';

interface PostComponentState {
  isEditing: boolean;
}
class PostEdit {
  title: string;
  fileType: string;
  contents: string;
  constructor(title: string, fileType: string, contents: string) {
    this.title = title;
    this.fileType = fileType;
    this.contents = contents;
  }
}

export class Post extends React.Component<PostProps, PostComponentState> {
  constructor(props: PostProps) {
    super(props);
    this.state = { isEditing: false };
  }

  startEdit = (e: any) => {
    console.dir(e);
    this.setState({ isEditing: true });
  }

  submitEdit = (e: any, editedPost: PostEdit) => {
    console.dir(e);
    this.setState({ isEditing: false });

    const { title, fileType, contents } = editedPost;
    const token = this.props.value.token;
    if (token) {
      this.props.actions.save(title, fileType, contents, token);
    } else {
      this.props.actions.onError("User not logged in.");
    }
  }

  cancelEdit = (e: any) => {
    console.dir(e);
    this.setState({ isEditing: false });
  }

  render() {
    const { value: props, actions: actions } = this.props;
    if (!props.error && !props.loading && !props.post && props.match) {
      const id: string = props.match.params.id;
      console.log('from route! id: ' + id);
      console.dir(props);
      actions.show(id);
    }
    return (
      <div>
        <Context {...this.props} />
        {(props.post)
          ? ((this.state.isEditing)
            ? <PostItemEdit {...{ post: props.post, submitEdit: this.submitEdit, cancelEdit: this.cancelEdit }} />
            : <PostItem {...{ post: props.post, startEdit: this.startEdit }} />)
          : (props.loading || props.error) ? null : <NotFound />}
      </div>
    );
  };
}

class PostItemEdit extends React.Component<{ post: PostModel, submitEdit: any, cancelEdit: any }, { postEdit: PostEdit, editor: any }> {
  constructor(props: { post: PostModel, submitEdit: any, cancelEdit: any }) {
    super(props);
    console.log("PostItemEdit");
    this.state = { postEdit: new PostEdit(props.post.title, props.post.fileType, props.post.contents), editor: null };
    console.dir(this.state);
  }

  registerEditor = (editor: any) => {
    this.setState({ editor: editor });
    // initial contents
    editor.insert(this.state.postEdit.contents);
    console.log("register!");
    console.dir(this.state);
  };

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
    if (!this.state.editor) { return ; }
    console.dir(event);
    console.dir(this.state);
    const edit = this.state.postEdit;
    edit.contents = value;
    this.setState({ postEdit: edit });
  }

  render() {
    const { post, submitEdit, cancelEdit } = this.props;
    return (
      <div>
        <textarea onChange={this.onTitleChange}>{post.title}</textarea>
        <textarea onChange={this.onFileTypeChange}>{post.fileType}</textarea>
        <AceEditor
          mode={post.fileType}
          theme="github"
          editorProps={{ $blockScrolling: true }}
          onLoad={(e) => this.registerEditor(e)}
          onChange={(value, event) => this.onContentsChange(value, event)}
        />
        <Link to="#" onClick={(e) => submitEdit(e, this.state.postEdit)}>Submit</Link>
        <Link to="#" onClick={(e) => cancelEdit(e)}>Cancel</Link>
      </div>
    );
  };
}


const PostItem: React.StatelessComponent<{ post: PostModel, startEdit: any }> =
  (props: { post: PostModel, startEdit: any }) => {
    const { post, startEdit } = props;
    console.log("PostItem");
    console.dir(post);
    return (
      <div>
        <p>{post.title}[{post.fileType}]</p>
        <code>{post.contents}</code>
        <Link to={"#"} onClick={(e) => startEdit(e)}>Edit</Link>
      </div>
    );
  };
