import { State } from '../state';
import { User, Token } from './User';

export interface UserState extends State {
  user: User | null;
  token: Token | null;
}

// empty state
export const initialState: UserState = { loading: false, error: null, user: null, token: null };
