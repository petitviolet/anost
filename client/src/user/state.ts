import { State } from '../state';
import { User } from './model/User';
import { Token } from './model/Token';

export interface UserState extends State {
  user: User | null;
  token: Token | null;
}
export const saveLocal = (state: UserState): void => {
  localStorage.setItem('user', JSON.stringify(state.user));
  localStorage.setItem('token', JSON.stringify(state.token));
};
export const clearLocal = (): void => {
  localStorage.removeItem('user');
  localStorage.removeItem('token');
};
const loadFromLocal = (): { user: User | null, token: Token | null } => {
  const userStorage = localStorage.getItem('user');
  const tokenStorage = localStorage.getItem('token');
  const user: User | null = (userStorage) ? JSON.parse(userStorage) : null;
  const token: Token | null = (tokenStorage) ? JSON.parse(tokenStorage) : null;
  return { user: user, token: token };
};

// empty state
const createInitialState = (): UserState => {
  const { user, token } = loadFromLocal();
  return { loading: false, error: null, user: user, token: token };
};
export const initialState = createInitialState();
