import React from 'react';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';
import ReactDOM from 'react-dom';
import { IntlProvider, addLocaleData } from 'react-intl';
import pt from 'react-intl/locale-data/pt';
import en from 'react-intl/locale-data/en';
import es from 'react-intl/locale-data/es';
import TopBar from './topBar';
import StaticPage from './staticPage';
import './resources/css/style.css';
import './resources/css/ldod.css';
import './resources/css/font-awesome.min.css';
import './resources/css/bootstrap.min.css';

import localeData from './resources/locales/data.json';

addLocaleData(pt);
addLocaleData(es);
addLocaleData(en);
// addLocaleData([pt, en, es]);

// Define user's language. Different browsers have the user locale defined
// on different fields on the `navigator` object, so we make sure to account
// for these different by checking all of them
const language = (navigator.languages && navigator.languages[0]) || navigator.language;

// Split locales with a region code
const languageWithoutRegionCode = language.toLowerCase().split(/[_-]+/)[0];

// Try full locale, try locale without region code, fallback to 'en'
const messages = localeData[languageWithoutRegionCode] || localeData[language] || localeData['pt-PT'];

class UpdateableIntlProvider extends React.Component {
    static childContextTypes = {
        updateLocale: React.PropTypes.func,
    };

    constructor(props) {
        super(props);
        this.state = {
            locale: props.locale,
            myMessages: props.messages,
        };
        console.log(typeof language);
    }

    getChildContext() {
        return { updateLocale: this.updateLocale };
    }

    updateLocale = (locale) => {
        const myMessages = localeData[locale];
        this.setState({ locale, myMessages });
    }

    render() {
        return (
            <IntlProvider
                messages={this.state.myMessages}
                locale={this.state.locale}>
                {this.props.children}
            </IntlProvider>
        );
    }
}

function App() {
    return (
        <UpdateableIntlProvider locale={language} messages={messages}>
            <Router>
                <div>
                    <TopBar />
                    <Switch>
                        <Route path={'/about/archive'} render={() => <StaticPage url={'/about/archive'} />} />
                        <Route path={'/about/videos'} render={() => <StaticPage url={'/about/videos'} />} />
                        <Route path={'/about/encoding'} render={() => <StaticPage url={'/about/encoding'} />} />
                        <Route path={'/about/articles'} render={() => <StaticPage url={'/about/articles'} />} />
                        <Route path={'/about/conduct'} render={() => <StaticPage url={'/about/conduct'} />} />
                        <Route path={'/about/privacy'} render={() => <StaticPage url={'/about/privacy'} />} />
                        <Route path={'/about/team'} render={() => <StaticPage url={'/about/team'} />} />
                        <Route path={'/about/acknowledgements'} render={() => <StaticPage url={'/about/acknowledgements'} />} />
                        <Route path={'/about/contact'} render={() => <StaticPage url={'/about/contact'} />} />
                        <Route path={'/about/copyright'} render={() => <StaticPage url={'/about/copyright'} />} />
                        <Route exact path={'/'} render={() => <StaticPage url={'/'} />} />
                    </Switch>
                </div>
            </Router>
        </UpdateableIntlProvider>
    );
}


ReactDOM.render(
    <App />,
    document.getElementById('root'),
);

