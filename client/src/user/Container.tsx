import { Dispatch } from 'redux';
import { ReduxAction, ReduxState } from '../store';
import * as actions from './actions';
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
    
  private onError(msg: string | Error): void {     
    const err: Error = (typeof(msg) == 'string') ? new Error(msg) : msg
    this.dispatch(actions.errorAction(err));
  }
  
  public logout(): void {
    this.dispatch(actions.logoutAction());
  }
  
  public async login(email: string, password: string): Promise<void> {
    const self = this;
    self.dispatch(actions.clearErrorAction());
    if (!email || !password) {
      self.onError("Email and Password must not be empty.");
      return Promise.resolve();
    }

    const path = `${UserPath.LOGIN}?email=${email}&password=${password}`;
    console.log(path);

    self.dispatch(actions.startRequestAction());
    const p = apiRequest(HttpMethod.GET, path, '')
    .then(res => {
      self.dispatch(actions.finishRequestAction())
      // cast
      const loginResponse: LoginResponse  = res;
      // if id exists, request/response are correct.
      if (loginResponse.hasOwnProperty('id')) {
        console.dir(loginResponse);
        self.dispatch(actions.loginAction(toUser(loginResponse)));
      } else {
        console.log('token undifined');
        self.onError("Login failed!");
      }
    })
    .catch(error => {
      console.log('ERROR: ' + error);
      self.onError(error)
    });
    return p;
  }
}

export default connect(
  (state: ReduxState) => ({ value: state.user }),
  (dispatch: Dispatch<ReduxAction>) => ({actions: new UserActionDispatcher(dispatch)})
)(LoginForm);
