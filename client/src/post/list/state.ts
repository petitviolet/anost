import { Post } from '../model/Post';
import { State } from '../../state';

export interface PostListState extends State {
  items: Post[];
}

export const initialPostListState: PostListState = { loading: false, error: null, items: [] };
