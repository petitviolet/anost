import * as React from 'react';
import { PostProps } from '../action/PostItemAction';
import { Post as PostModel } from '../model/Post';
import { Context } from './Context';
import { NotFound } from './NotFound';
import AceEditor from 'react-ace';
import { Link } from 'react-router-dom';
import 'brace/theme/monokai';

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

    const { id, title, fileType, contents } = editedPost;
    const token = this.props.value.token;
    if (token) {
      this.props.actions.update(id, title, fileType, contents, token);
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

// a component for editing Post
class PostItemEdit extends React.Component<{ post: PostModel, submitEdit: any, cancelEdit: any }, { postEdit: PostEdit, editor: any }> {
  constructor(props: { post: PostModel, submitEdit: any, cancelEdit: any }) {
    super(props);
    console.log("PostItemEdit");
    this.state = { postEdit: new PostEdit(props.post.id, props.post.title, props.post.fileType, props.post.contents), editor: null };
    require(`brace/mode/${props.post.fileType}`)
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
    console.dir(this.state.postEdit);
  }

  render() {
    const { submitEdit, cancelEdit } = this.props;
    const post = this.state.postEdit;
    const inputStyle = { fontSize: '18px', paddingBottom: '5px', margin: '8px', border: 'none', borderBottom: 'solid 2px blue' };
    return (
      <div>
        <input type="text" placeholder="file name" style={inputStyle} onChange={this.onTitleChange} value={post.title} />
        <input type="text" placeholder="file type" style={inputStyle} onChange={this.onFileTypeChange} value={post.fileType} />
        <PostItemEditor {...{ post: post, onChange: this.onContentsChange }} />
        <Link to="#" onClick={(e) => {
          submitEdit(e, this.state.postEdit)
        }}><div style={{ color: "blue", paddint: "3px" }}>Submit</div></Link>
        <Link to="#" onClick={(e) => cancelEdit(e)}><div style={{ color: "red", paddint: "3px" }}>Cancel</div></Link>
      </div>
    );
  };
}

const PostItemEditor: React.StatelessComponent<{ post: PostEdit, onChange: any }> =
  (props: { post: PostEdit, onChange: any }) => {
    return createEditor(props.post, true, props.onChange);
  }


const PostItem: React.StatelessComponent<{ post: PostModel, startEdit: any }> =
  (props: { post: PostModel, startEdit: any }) => {
    const { post, startEdit } = props;
    console.dir(post);
    return (
      <div>
        <p>{post.title}[{post.fileType}]</p>
        <Link to={"#"} onClick={startEdit}>Edit</Link>
        <PostItemViewer {...post} />
      </div>
    );
  };

const PostItemViewer: React.StatelessComponent<PostModel> =
  (post: PostModel) => {
    return createEditor(post, true);
  };


 interface editorData {
   fileType: string;
   title: string;
   contents: string;
 }
 const createEditor = (post: editorData, readOnly: boolean, onChange?: (value: string) => void): JSX.Element => {
    try {
      require(`brace/mode/${post.fileType}`)
      console.log(`new mode: ${post.fileType}`);
    } catch (e) {
      console.log('error new mode: ' + e);
    }
    const lines = function(){
      const lineNum = post.contents.split('\n').length;
      return (lineNum < 50) ? lineNum : 50;
    }();
    return (
      <AceEditor
        mode={post.fileType}
        theme="monokai"
        value={post.contents}
        minLines={lines}
        maxLines={lines}
        readOnly={readOnly}
        focus={false}
        onChange={onChange}
      />
    )
 };