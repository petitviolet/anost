import * as React from 'react';
import { Comment as CommentModel } from '../model/Post';

export const Comment: React.StatelessComponent<CommentModel> =
    (props: CommentModel) => {
      return (
        <div>
          <table>
          <thead>
            <tr>
              <th>User</th>
              <th>Comment</th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <td>{props.userId}</td>
              <td>{props.sentence}</td>
            </tr>
          </tbody>
          </table>
        </div>
      );
    };
