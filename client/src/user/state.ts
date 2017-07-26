import { User, Token } from './User';

export interface UserState {
  loading: boolean;  // duration of sending request
  error: Error | null; // something error occurred or not
  user: User | null;
  token: Token | null;
}

// empty state
export const initialState: UserState = { loading: false, error: null, user: null, token: null };
