import { match } from 'react-router-dom';
import * as H from 'history';

export interface State {
  loading: boolean;  // duration of sending request
  error: Error | null; // something error occurred or not
  match?: match<any>;
  location?: H.Location;
  history?: H.History;
  staticContext?: any;
}
