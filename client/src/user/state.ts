import {User} from './User';

export interface UserState {
  user: User | null;
}

// with empty token
export const initialState: UserState = { user: null };
