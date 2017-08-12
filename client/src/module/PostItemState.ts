import { Post } from '../model';
import { State } from './state';

export interface PostState extends State {
  postId: string;
  post: Post | null;
}

export const initialPostState: PostState = { postId: '', post: null };