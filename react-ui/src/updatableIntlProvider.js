import PropTypes from 'prop-types';
import React from 'react';
import en from 'react-intl/locale-data/en';
import es from 'react-intl/locale-data/es';
import pt from 'react-intl/locale-data/pt';
import { addLocaleData, IntlProvider } from 'react-intl';
import localeData from './resources/locales/data.json';


addLocaleData(pt);
addLocaleData(es);
addLocaleData(en);


export default class UpdatableIntlProvider extends React.Component {

    static childContextTypes = {
        updateLocale: PropTypes.func,
    };

    constructor(props) {
        super(props);
        // Define user's language. Different browsers have the user locale defined
        // on different fields on the `navigator` object, so we make sure to account
        // for these different by checking all of them
        const language = (navigator.languages && navigator.languages[0]) || navigator.language;
        // Split locales with a region code
        const languageWithoutRegionCode = language.toLowerCase().split(/[_-]+/)[0];
        // Try full locale, try locale without region code, fallback to 'en'
        const messages = localeData[languageWithoutRegionCode] || localeData[language] || localeData['pt-PT'];
        this.state = {
            locale: language,
            myMessages: messages,
        };
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
