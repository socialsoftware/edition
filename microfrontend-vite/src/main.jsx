import React from 'react';
import ReactDOM from 'react-dom';
import AppRouter from './AppRouter';
import { BrowserRouter as Router } from 'react-router-dom'


ReactDOM.render(
  <Router basename={import.meta.env.VITE_BASE_PATH}>
      <AppRouter />
  </Router>,
  document.getElementById('root')
);
