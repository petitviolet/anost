import * as React from 'react';
import './App.css';
// import createBrowserHistory from 'history/createBrowserHistory';
import { BrowserRouter, Route, Switch } from 'react-router-dom';

import User from './container/UserContainer';
import PostList from './container/PostListContainer';
import Header from './container/HeaderContainer';
import { LoginForm, NotFound } from './component';

// const logo = require('./logo.svg');

export default class App extends React.Component {
  render() {
    // const history = createBrowserHistory();
    return (
      <BrowserRouter>
        <div className="App">
          <div className="App-header">
            <div className="App-header-inner">
              <Header />
            </div>
          </div>
          <div className="App-Content">
            <Switch>
              <Route exact path="/" component={User} />
              <Route exact path="/user/*" component={User} />
              <Route path="/posts/*" component={PostList} />
              <Route exact path="/login" component={LoginForm} />
              <Route component={NotFound} />
            </Switch>
          </div>
        </div>
      </BrowserRouter>
    );
  }
}
