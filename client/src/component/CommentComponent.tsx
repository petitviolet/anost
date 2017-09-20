import * as React from 'react';
import { Comment as CommentModel } from '../model';
import { Link } from 'react-router-dom';
import { LoginInfo, PostProps } from '../module/';
import { PostActionDispatcher } from '../module/PostItemDispatcher';

export const Comments: React.StatelessComponent<PostProps> =
  (props: PostProps) => {
    const { comments: comments } = props.value.post!;
    console.log('comments');
    console.dir(comments);
    return (
      <div>
        {(comments.length > 0) ?
          comments.map((comment, idx) => <Comment key={idx} {...{comment: comment, actions: props.actions, login: props.value.login}} />)
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
        {(propsValue.post && propsValue.login) ?
          <div>
            <Link to="#" onClick={(e) => {
              actions.addComment(propsValue.post!, this.state.input, propsValue.login!.token);
              this.setState({ input: '' });
            }}>Add comment</Link>
          </div>
          : null
        }
      </div>
    );
  }
}

interface CommentProps {
  comment: CommentModel;
  actions: PostActionDispatcher;
  login?: LoginInfo;
}

const Comment: React.StatelessComponent<CommentProps> =
  (props: CommentProps) => {
    const { comment: comment, actions: actions, login: login } = props;
    return (
      <div style={commentStyle}>
        <div style={commentUserStyle}>
          Mr. {comment.owner.userName.charAt(0)}
          {(login && comment.owner.userId === login.user.id) ?
              <input style={commentDeleteButtonStyle} type="button" onClick={(e) => actions.deleteComment(comment, login.token)} value="☓"/>
              : null
            }
        </div>
        <div>{comment.sentence}</div>
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
};

const commentDeleteButtonStyle = {
  backgroundColor: 'rgba(240, 240, 240, 220)',
  float: 'right',
  border: '1px solid #EEEEEE',
};