import * as React from 'react';
import './App.css';
import createBrowserHistory from 'history/createBrowserHistory';
import { Router, Route, Switch } from 'react-router-dom';

import User from './user/Container';
import { LoginForm } from './user/LoginForm';
import Header from './component/Header';
import { NotFound } from './component/NotFound';

// const logo = require('./logo.svg');

class App extends React.Component {
  render() {
    const history = createBrowserHistory();
    return (
      <Router history={history}>
        <div className="App">
          <div className="App-header">
            <Header />
          </div>
          <div className="App-Content">
            <Switch>
              <Route path="/" component={User} />
              <Route exact path="/login" component={LoginForm} />
              <Route component={NotFound} />
            </Switch>
          </div>
        </div>
      </Router>
    );
  }
}

export default App;