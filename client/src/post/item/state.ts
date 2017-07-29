import { Post } from '../model/Post';
import { State } from '../../state';

export interface PostState extends State {
  post: Post | null;
}

export const initialPostState: PostState = { loading: false, error: null, post: null };