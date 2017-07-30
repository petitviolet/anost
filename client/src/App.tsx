import * as React from 'react';
import './App.css';
import createBrowserHistory from 'history/createBrowserHistory';
import { Router, Route, Switch } from 'react-router-dom';

import User from './user/Container';
import { LoginForm } from './user/LoginForm';
import Header from './component/Header';
import { NotFound } from './component/NotFound';
import PostList from './post/list/Container';

// const logo = require('./logo.svg');

class App extends React.Component {
  render() {
    const history = createBrowserHistory();
    return (
      <Router history={history}>
        <div className="App">
          <div className="App-header">
            <div className="App-header-inner">
              <Header />
            </div>
          </div>
          <div className="App-Content">
            <Route path="/" >
              <Switch>
                <Route path="/" component={User} />
                <Route path="/user/me/posts" component={PostList} />
                <Route exact path="/login" component={LoginForm} />
                <Route component={NotFound} />
              </Switch>
            </Route>
          </div>
        </div>
      </Router>
    );
  }
}

export default App;