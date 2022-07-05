import React from 'react';
import ReactDOM from 'react-dom/client';
import AppRouter from './AppRouter';
import { BrowserRouter as Router } from 'react-router-dom';

const root = ReactDOM.createRoot(document.getElementById('root'));

root.render(
  <Router basename={import.meta.env.VITE_BASE_PATH}>
    <AppRouter />
  </Router>
);
