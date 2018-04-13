import React from 'react';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';
import ReactDOM from 'react-dom';
import TopBar from './topBar';
import StaticPage from './staticPage';
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
                        <Route path={'/about/archive'} render={() => <StaticPage url={'/about/archive'} />} />
                        <Route path={'/about/videos'} render={() => <StaticPage url={'/about/videos'} />} />
                        <Route path={'/about/faq'} render={() => <StaticPage url={'/about/faq'} />} />
                        <Route path={'/about/encoding'} render={() => <StaticPage url={'/about/encoding'} />} />
                        <Route path={'/about/articles'} render={() => <StaticPage url={'/about/articles'} />} />
                        <Route path={'/about/conduct'} render={() => <StaticPage url={'/about/conduct'} />} />
                        <Route path={'/about/privacy'} render={() => <StaticPage url={'/about/privacy'} />} />
                        <Route path={'/about/team'} render={() => <StaticPage url={'/about/team'} />} />
                        <Route path={'/about/acknowledgements'} render={() => <StaticPage url={'/about/acknowledgements'} />} />
                        <Route path={'/about/contact'} render={() => <StaticPage url={'/about/contact'} />} />
                        <Route path={'/about/copyright'} render={() => <StaticPage url={'/about/copyright'} />} />
                        <Route path={'/reading'} render={() => <StaticPage url={'/reading'} />} />
                        <Route exact path={'/'} render={() => <StaticPage url={'/'} />} />
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

