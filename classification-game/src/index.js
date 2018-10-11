import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import App from './app/App';
import { BrowserRouter as Router } from 'react-router-dom';
import 'font-awesome/css/font-awesome.min.css';
import 'bootstrap-social/bootstrap-social.css';

ReactDOM.render(
    <Router basename={'classification-game'}>
        <App />

    </Router>,
    document.getElementById('root')
);