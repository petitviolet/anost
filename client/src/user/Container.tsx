import { Dispatch } from 'redux';
import { ReduxAction, ReduxState } from '../store';
import { saveTokenAction, loginAction } from './actions';
import { apiRequest, HttpMethod } from '../util/request';
import { connect } from 'react-redux';
// import { User } from './user';
import { LoginForm } from './LoginForm';

enum UserPath {
  LOGIN = '/user/login'
}

interface LoginResponse {
  value: string
}

export class UserActionDispatcher {
  constructor(private dispatch: (action: ReduxAction) => void) {}

  public saveToken(token: string): void {
    this.dispatch(saveTokenAction(token));
  }

  public async login(email: string, password: string): Promise<void> {
    const self = this;
    const path = `${UserPath.LOGIN}?email=${email}&password=${password}`;
    console.log(path);

    const p = apiRequest(HttpMethod.GET, path, '')
    .then(res => {
      const token: LoginResponse  = res;
      if (token.hasOwnProperty('value')) {
        console.dir(token);
        self.dispatch(loginAction(token.value));
      } else {
        console.log('token undifined');
      }
    })
    .catch(error => {
      console.log('ERROR: ' + error);
    });
    return p;
  }
}

export default connect(
  (state: ReduxState) => ({ value: state.user }),
  (dispatch: Dispatch<ReduxAction>) => ({actions: new UserActionDispatcher(dispatch)})
)(LoginForm);
