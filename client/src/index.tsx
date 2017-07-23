import * as React from 'react';
import * as ReactDOM from 'react-dom';
// import App from './App';
import registerServiceWorker from './registerServiceWorker';
import './index.css';
import { Provider } from 'react-redux';
import store from './store';
import User from './user/Container';

ReactDOM.render(
  <Provider store={store}>
   <User />
  </Provider>,
  // <App />,
  document.getElementById('root') as HTMLElement
);
registerServiceWorker();
