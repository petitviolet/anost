export interface UserState {
  name: string;
  email: string;
  token: string;
}

// with empty token
export const initialState: UserState = { name: '', email: '', token: '' };
