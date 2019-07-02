import React from 'react';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';
import ReactDOM from 'react-dom';
import { Provider } from 'react-redux';
import { createStore } from 'redux';
import TopBar from './TopBar';
import LegacyPage from './legacyPage';
import UpdatableIntlProvider from './updatableIntlProvider';
import FragmentMain from './components/fragment/FragmentMain';
import rootReducer, { initialState } from './reducers/reducers';
import Login from './components/Login';
import ChangePassword from './components/ChangePassword';

function App() {
    const store = createStore(rootReducer, loadState());

    console.log(store.getState());

    store.subscribe(() => {
        console.log(store.getState());
        saveState(store.getState());
    });

    return (
        <Provider store={store}>
            <UpdatableIntlProvider>
                <Router>
                    <div>
                        <TopBar />
                        <Switch>
                            <Route exact path="/user/changePassword" component={ChangePassword} />
                            <Route exact path="/signin" component={Login} />
                            <Route exact path="/fragments/fragment/:fragId/inter/:interId" component={FragmentMain} />
                            <Route exact path="/fragments/fragment/:fragId" component={FragmentMain} />
                            <Route exact path={'/'} render={() => <LegacyPage url={'/'} />} />
                            <Route render={props => <LegacyPage url={props.location.pathname} />} />
                        </Switch>
                    </div>
                </Router>
            </UpdatableIntlProvider>
        </Provider>
    );
}

function saveState(state) {
    try {
        const stateString = JSON.stringify(state);
        sessionStorage.setItem('state', stateString);
    } catch (e) {
        console.log(e);
    }
}

function loadState() {
    try {
        const stateString = sessionStorage.getItem('state');

        if (stateString === null) {
            return initialState;
        }

        return JSON.parse(stateString);
    } catch (e) {
        return initialState;
    }
}


ReactDOM.render(
    <App />,
    document.getElementById('root'),
);

