import * as React from 'react';
import './App.css';
import createBrowserHistory from 'history/createBrowserHistory';
import {Router} from 'react-router-dom';

import User from './user/Container';
// import Post from './post/item/Container';
import Header from './component/Header';

// const logo = require('./logo.svg');

class App extends React.Component {
  render() {
    const history = createBrowserHistory();
    return (
      <div className="App">
        <div className="App-header">
          <Header />
        </div>
        <div className="App-Content">
          <Router history={history}>
            <User />
          </Router>
        </div>
      </div>
    );
  }
}

export default App;