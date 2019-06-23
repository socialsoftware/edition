import React from 'react';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';
import ReactDOM from 'react-dom';
import { Provider } from 'react-redux';
import { createStore } from 'redux';
import TopBar from './TopBar';
import LegacyPage from './legacyPage';
import UpdatableIntlProvider from './updatableIntlProvider';
import { FragmentMain } from './components/fragment/FragmentMain';
import rootReducer from './reducers/reducers';

function App() {
    const store = createStore(rootReducer);

    console.log(store.getState());

    store.subscribe(() => console.log(store.getState()));

    return (
        <Provider store={store}>
            <UpdatableIntlProvider>
                <Router>
                    <div>
                        <TopBar />
                        <Switch>
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


ReactDOM.render(
    <App />,
    document.getElementById('root'),
);

