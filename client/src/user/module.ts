import { ActionNames, UserActions } from './actions';
import { UserState, initialState } from './state';

// reducer endpoint for user
export default function reducer(state: UserState = initialState, action: UserActions): UserState {
  switch (action.type) {
    case ActionNames.SAVE_TOKEN_NAME:
      return Object.assign({}, state, { token: action.newToken });
    case ActionNames.LOGIN:
      return Object.assign({}, state, { token: action.newToken });
    default:
      return state;
  }
}
