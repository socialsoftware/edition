import React from 'react';
import { injectIntl } from 'react-intl';
import customHTMLParser from './customHTMLParser';


class LegacyPage extends React.Component {

    static baseURL = 'http://localhost:8080';

    constructor(props) {
        super(props);
        const langConst = `?lang=${props.intl.locale.split(/[_-]+/)[0]}`;
        this.state = {
            error: null,
            isLoaded: false,
            html: '',
            lang: langConst,
            url: LegacyPage.baseURL + props.url,
        };
    }


    htmlRequest(url) {
        fetch(url)
            .then(res => res.text())
            .then(
                (result) => {
                    this.setState({
                        isLoaded: true,
                        // The response is an array with only 1 element, the element body
                        html: result,
                    });
                },
                (error) => {
                    this.setState({
                        isLoaded: true,
                        error,
                    });
                });
    }

    componentDidMount() {
        this.htmlRequest(this.state.url + this.state.lang);
        window.scrollTo(0, 0);
    }

    componentWillReceiveProps(nextProps) {
        const state = this.state;
        state.url = LegacyPage.baseURL + nextProps.url;
        state.lang = `?lang=${nextProps.intl.locale.split(/[_-]+/)[0]}`;
        this.htmlRequest(state.url + state.lang);
        this.setState(state);
        window.scrollTo(0, 0);
    }

    render() {
        const { error, isLoaded, html } = this.state;
        const parsedHTML = customHTMLParser(html);
        if (this.state.error) {
            return <div>Error: {error.message}</div>;
        } else if (!isLoaded) {
            return <div>Loading LdoD...</div>;
        }
        const remaining = parsedHTML.props.children.splice(1, parsedHTML.props.children.length);
        const scripts = remaining.splice(1, remaining.length);
        return (
            <div className={'container ldod-default'}>
                {remaining[0].props.children}
                {scripts}
            </div>
        );
    }
}


export default injectIntl(LegacyPage);
