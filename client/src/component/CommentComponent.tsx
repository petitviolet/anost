import * as React from 'react';
import { Comment as CommentModel } from '../model';
import { Link } from 'react-router-dom';
import { PostProps } from '../module/';

export const Comments: React.StatelessComponent<PostProps> =
  (props: PostProps) => {
    const { comments: comments } = props.value.post!;
    console.log('comments');
    console.dir(comments);
    return (
      <div>
        {(comments.length > 0) ?
          comments.map((comment, idx) => <Comment key={idx} {...comment} />)
          : <div>no comments.</div>
        }
        <AddComment {...props} />
      </div>
    );
  };

class AddComment extends React.Component<PostProps, { input: string }> {
  constructor(props: PostProps) {
    super(props);
    this.state = { input: '' };
  }

  onInputChange = (e: any) => {
    this.setState({ input: e.target.value });
  }

  render() {
    console.log(this.props);
    const { value: propsValue, actions: actions } = this.props;
    return (
      <div>
        <input type="text" placeholder="comment" value={this.state.input} onChange={this.onInputChange} />
        {(propsValue.post && propsValue.token) ?
          <div>
            <Link to="#" onClick={(e) => {
              actions.addComment(propsValue.post!, this.state.input, propsValue.token!);
              this.setState({ input: '' });
            }}>Add comment</Link>
          </div>
          : null
        }
      </div>
    );
  }
}

const Comment: React.StatelessComponent<CommentModel> =
  (props: CommentModel) => {
    return (
      <div style={commentStyle}>
        <div style={commentUserStyle}>Mr. {props.owner.userName.charAt(0)}</div>
        <div>{props.sentence}</div>
      </div>
    );
  };

const commentStyle = {
  border: '1px solid #E0E0E0',
  padding: '8px',
  marginTop: '5px',
  width: '60%',
};
const commentUserStyle = {
  textDecoration: 'underline',
}
