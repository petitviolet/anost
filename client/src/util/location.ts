import { push } from 'react-router-redux'
import store from '../store';

export const locationPush = (path: string) => {
  store.dispatch(push(path));
};