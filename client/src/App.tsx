import * as React from 'react';
import './App.css';
// import { ConnectedRouter } from 'react-router-redux';
import { Router, Route, Switch } from 'react-router-dom';
import { Provider } from 'react-redux';

import User from './container/UserContainer';
import PostList from './container/PostListContainer';
import Header from './container/HeaderContainer';
import PostItem from './container/PostItemContainer';
import NewPostItem from './container/NewPostItemContainer';
import LoginForm from './container/LoginContainer';
import { NotFound } from './component';
import store, { history } from './store';

export default class App extends React.Component {
  render() {
    return (
      <Provider store={store}>
        <Router history={history}>
          <div className="App">
            <div className="App-header">
              <div className="App-header-inner">
                <Header />
              </div>
            </div>
            <div className="App-Content">
              <Switch>
                <Route exact path="/" component={PostList} />
                <Route exact path="/login" component={LoginForm} />
                <Route exact path="/user/*" component={User} />
                <Route path="/posts/user" component={PostList} />
                <Route path="/posts/search" component={PostList} />
                <Route exact path="/posts/new" component={NewPostItem} />
                <Route path="/posts/:id" component={PostItem} />
                <Route component={NotFound} />
              </Switch>
            </div>
          </div>
        </Router>
      </Provider>
    );
  }
}
