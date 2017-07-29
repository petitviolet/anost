import * as React from 'react';
import './App.css';

import User from './user/Container';
// import Post from './post/item/Container';
import Header from './component/Header';

// const logo = require('./logo.svg');

class App extends React.Component {
  render() {
    return (
      <div className="App">
        <div className="App-header">
          <Header />
        </div>
        <div className="App-Content">
          <User />
        </div>
      </div>
    );
  }
}

export default App;