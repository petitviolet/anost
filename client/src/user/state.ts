import {User} from './User';

export interface UserState {
  loading: boolean,  // duration of sending request
  user: User | null;
}

// with empty token
export const initialState: UserState = { loading: false, user: null };
