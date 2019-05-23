import React from 'react';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';
import ReactDOM from 'react-dom';
import TopBar from './topBar';
import LegacyPage from './legacyPage';
import UpdatableIntlProvider from './updatableIntlProvider';
import { FragmentMain } from './components/FragmentMain';

function App() {
    return (
        <UpdatableIntlProvider>
            <Router>
                <div>
                    <TopBar />
                    <Switch>
                        <Route path="fragments/fragment/:fragId" component={FragmentMain} />
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

