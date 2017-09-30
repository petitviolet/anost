import { Post } from '../model';
import { State } from './state';

export interface PostState extends State {
  postId: string;
  post: Post | null;
  isEditing: boolean;
}

export const initialPostState: PostState = { postId: '', post: null, isEditing: false };