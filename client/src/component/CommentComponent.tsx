import * as React from 'react';
import { Comment as CommentModel } from '../model/Post';
import { Link } from 'react-router-dom';

export const Comments: React.StatelessComponent<{comments: CommentModel[]}> =
  (props: {comments: CommentModel[]}) => {
    const { comments } = props
    console.log('comments');
    console.dir(props);
    console.dir(comments);
    return (
      <div>
        {(comments.length > 0) ?
          comments.map((comment, idx) => <Comment key={idx} {...comment} />)
          : <div>no comments.</div>
        }
        <div>
          <Link to="#" onClick={(e) => {
            // submitEdit(e, this.state.postEdit);
          }}>Add comment</Link>
        </div>
      </div>
    );
  };

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
