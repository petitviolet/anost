import { match } from 'react-router-dom';
import * as H from 'history';
import { User, Token } from '../model';

export interface State {
  loading?: boolean;  // duration of sending request
  error?: Error; // something error occurred or not
  login?: LoginInfo;
  match?: match<any>;
  location?: H.Location;
  history?: H.History;
  staticContext?: any;
}

export interface LoginInfo {
  user: User;
  token: Token;
}
