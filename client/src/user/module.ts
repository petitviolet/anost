import { UserAction, UserActions } from './actions';
import { UserState, initialState } from './state';

// reducer endpoint for user
export default function reducer(state: UserState = initialState, action: UserActions): UserState {
  switch (action.type) {
    case UserAction.REQUEST_START:
      return Object.assign({}, state, { loading: true }); 
    case UserAction.LOGIN:
      return Object.assign({}, state, { loading: false, user: action.user });
    default:
      return state;
  }
}
