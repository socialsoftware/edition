import React from 'react';
import { injectIntl } from 'react-intl';

class LanguageToggle extends React.Component {

    static contextTypes = {
        updateLocale: React.PropTypes.func,
    }

    constructor(props) {
        super(props);
        const locale = props.intl.locale.toLowerCase().split(/[_-]+/)[0];
        this.state = {
            active: {
                'pt-PT': locale === 'pt',
                'en-GB': locale === 'en',
                'es-ES': locale === 'es',
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
                <a className={this.state.active['pt-PT'] ? 'active' : ''} onClick={() => this.handleClick('pt-PT')}>PT</a>
                <a className={this.state.active['en-GB'] ? 'active' : ''} onClick={() => this.handleClick('en-GB')}>EN</a>
                <a className={this.state.active['es-ES'] ? 'active' : ''} onClick={() => this.handleClick('es-ES')}>ES</a>
            </li>
        );
    }

}

export default injectIntl(LanguageToggle);
