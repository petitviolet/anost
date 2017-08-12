import { Post } from '../model';
import { State } from './state';

export interface PostListState extends State {
  query: string;
  userId: string;
  items: Post[];
}

export const initialPostListState: PostListState = { query: '', userId: '', items: [] };
