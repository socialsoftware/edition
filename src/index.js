import React from 'react';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';
import ReactDOM from 'react-dom';
import TopBar from './topBar';
import LegacyPage from './legacyPage';
import UpdatableIntlProvider from './updatableIntlProvider';
import './resources/css/ldod.css';
import './resources/css/font-awesome.min.css';
import './resources/css/bootstrap.min.css';
import './resources/css/style.css';


function App() {
    return (
        <UpdatableIntlProvider>
            <Router>
                <div>
                    <TopBar />
                    <Switch>
                        <Route exact path={'/'} render={() => <LegacyPage url={'/'} />} />
                        <Route render={props => <LegacyPage url={props.location.pathname} />} />
                    </Switch>
                </div>
            </Router>
        </UpdatableIntlProvider>
    );
}


ReactDOM.render(
    <App />,
    document.getElementById('root'),
);

