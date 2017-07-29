import { State } from '../state';
import { User } from './model/User';
import { Token } from './model/Token';

export interface UserState extends State {
  user: User | null;
  token: Token | null;
}

// empty state
export const initialState: UserState = { loading: false, error: null, user: null, token: null };
