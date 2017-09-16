import * as React from 'react';
import { Comment as CommentModel } from '../model/Post';

export const Comment: React.StatelessComponent<CommentModel> =
    (props: CommentModel) => {
      return (
        <div style={commentStyle}>
          <div style={commentUserStyle}>Mr. {props.owner.userName.charAt(0)}</div>
          <div>{props.sentence}</div>
        </div>
      );
    };

const commentStyle = {
  border: "1px solid #999",
  margin: "2px",
};
const commentUserStyle = {
  textDecoration: "underline",
}
