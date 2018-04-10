import React from 'react';
import ReactHTMLParser from 'react-html-parser';
import { injectIntl } from 'react-intl';

class StaticPage extends React.Component {

    static baseURL = 'http://1.1.1.10:8080';

    constructor(props) {
        super(props);
        const langConst = `/?lang=${props.intl.locale.split(/[_-]+/)[0]}`;
        this.state = {
            error: null,
            isLoaded: false,
            html: '',
            lang: langConst,
            url: StaticPage.baseURL + props.url,
        };
    }


    htmlRequest(url) {
        console.log(url);
        fetch(url)
            .then(res => res.text())
            .then(
                (result) => {
                    this.setState({
                        isLoaded: true,
                        // The response is an array with only 1 element, the element body
                        html: ReactHTMLParser(result)[0],
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
        state.url = StaticPage.baseURL + nextProps.url;
        state.lang = `/?lang=${nextProps.intl.locale.split(/[_-]+/)[0]}`;
        this.htmlRequest(state.url + state.lang);
        this.setState(state);
        window.scrollTo(0, 0);
    }

    render() {
        const { error, isLoaded, html } = this.state;
        if (this.state.error) {
            return <div>Error: {error.message}</div>;
        } else if (!isLoaded) {
            return <div>Loading...</div>;
        }
        return (
            <div className={'container ldod-default'}>
                {/* The response comes with entire body. props.children removes the element body */}
                {html.props.children}
            </div>
        );
    }
}


export default injectIntl(StaticPage);
