import { State } from './state';
import { User, Token } from '../model';

export interface UserState extends State {
  user: User | null;
}
export const saveLocal = (state: UserState): void => {
  localStorage.setItem('user', JSON.stringify(state.user));
  localStorage.setItem('token', JSON.stringify(state.token));
};
export const clearLocal = (): void => {
  localStorage.removeItem('user');
  localStorage.removeItem('token');
};
const loadFromLocal = (): { user: User | null, token?: Token } => {
  const userStorage = localStorage.getItem('user');
  const tokenStorage = localStorage.getItem('token');
  const user: User | null = (userStorage) ? JSON.parse(userStorage) : null;
  const token: Token = (tokenStorage) ? JSON.parse(tokenStorage) : undefined;
  return { user: user, token: token };
};

// empty state
const createInitialState = (): UserState => {
  const { user, token } = loadFromLocal();
  return { user: user, token: token };
};
export const initialState = createInitialState();
