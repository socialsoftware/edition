import React from 'react';
import PropTypes from 'prop-types';
import { injectIntl } from 'react-intl';

class LanguageToggle extends React.Component {

    static contextTypes = {
        updateLocale: PropTypes.func,
    }

    constructor(props) {
        super(props);
        const locale = props.intl.locale.toLowerCase().split(/[_-]+/)[0];
        this.state = {
            active: {
                pt: locale === 'pt',
                en: locale === 'en',
                es: locale === 'es',
            },
        };
        this.handleClick = this.handleClick.bind(this);
    }

    handleClick(lang) {
        const active = this.state.active;
        Object.keys(active).forEach(value => active[value] = false);
        active[lang] = true;
        this.context.updateLocale(lang);
        this.setState(active);
    }

    render() {
        return (
            <li className={'nav-lang'}>
                <a className={this.state.active.pt ? 'active' : ''} onClick={() => this.handleClick('pt')}>PT</a>
                <a className={this.state.active.en ? 'active' : ''} onClick={() => this.handleClick('en')}>EN</a>
                <a className={this.state.active.es ? 'active' : ''} onClick={() => this.handleClick('es')}>ES</a>
            </li>
        );
    }

}

export default injectIntl(LanguageToggle);
