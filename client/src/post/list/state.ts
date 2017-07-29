import { Post } from '../model/Post';
import { State } from '../../state';

export interface PostListState extends State {
  query: string;
  userId: string;
  items: Post[];
}

export const initialPostListState: PostListState = { loading: false, error: null, query: '', userId: '', items: [] };
