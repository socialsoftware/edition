import React from 'react';
import { injectIntl } from 'react-intl';
import Helmet from 'react-helmet';
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
        console.log(html);
        console.log(parsedHTML);
        return (
            <div className={'container ldod-default'}>
                <Helmet>
                    <script src={'resources/js/jquery-3.3.1.min.js'} />
                </Helmet>
                <Helmet>
                    <script src={'resources/js/bootstrap.min.js'} />
                </Helmet>
                {parsedHTML.props.children}
            </div>
        );
    }
}


export default injectIntl(LegacyPage);
