import { UserAction, UserActions } from './actions';
import { UserState, initialState, clearLocal, saveLocal } from './state';

// reducer endpoint for user
export default function reducer(state: UserState = initialState, action: UserActions): UserState {
  switch (action.type) {
    case UserAction.REQUEST_START:
      return Object.assign({}, state, { loading: true });
    case UserAction.REQUEST_FINISH:
      return Object.assign({}, state, { loading: false });
    case UserAction.ERROR_OCCURRED:
    case UserAction.ERROR_CLEARD:
      return Object.assign({}, state, { error: action.error });
    case UserAction.LOGIN:
      const newState = Object.assign({}, state, { user: action.user, token: action.token });
      saveLocal(newState);
      return newState;
    case UserAction.LOGOUT:
      clearLocal();
      return Object.assign({}, state, { user: null });
    default:
      return state;
  }
}
