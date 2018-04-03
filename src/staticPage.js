import React from 'react';
import ReactHTMLParser from 'react-html-parser';
import i18next from './i18next';


export default class StaticPage extends React.Component {

    static baseURL = 'http://1.1.1.10:8080';

    constructor(props) {
        super(props);
        this.state = {
            error: null,
            isLoaded: false,
            html: '',
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
        this.htmlRequest(StaticPage.baseURL + this.props.url + i18next.t('lang'));
    }

    componentWillReceiveProps(nextProps) {
        this.htmlRequest(StaticPage.baseURL + nextProps.url + i18next.t('lang'));
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
