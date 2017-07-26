import { Dispatch } from 'redux';
import { ReduxAction, ReduxState } from '../store';
import { loginAction } from './actions';
import { apiRequest, HttpMethod } from '../util/request';
import { connect } from 'react-redux';
import { User, Token } from './User';
import { LoginForm } from './LoginForm';

enum UserPath {
  LOGIN = '/user/login'
}

interface LoginResponse {
  id: string
  name: string
  email: string
  token: Token
}

const toUser = (res: LoginResponse): User => { 
  return new User(res.id, res.name, res.email, res.token); 
};

export class UserActionDispatcher {
  constructor(private dispatch: (action: ReduxAction) => void) {}

  public async login(email: string, password: string): Promise<void> {
    const self = this;
    const path = `${UserPath.LOGIN}?email=${email}&password=${password}`;
    console.log(path);

    const p = apiRequest(HttpMethod.GET, path, '')
    .then(res => {
      // cast
      const loginResponse: LoginResponse  = res;
      // if id exists, request/response are correct.
      if (loginResponse.hasOwnProperty('id')) {
        console.dir(loginResponse);
        self.dispatch(loginAction(toUser(loginResponse)));
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
