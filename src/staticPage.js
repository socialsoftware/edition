import React from 'react';
import { Link } from 'react-router-dom';
import ReactHTMLParser, { convertNodeToElement } from 'react-html-parser';
import { injectIntl } from 'react-intl';


function transform(node) {
    if (node.type === 'tag' && node.name === 'a' && node.attribs.href) {
        return (<Link
            to={node.attribs.href}>
            {node.children.map((child, index) => convertNodeToElement(child, index, transform))}
        </Link>);
    }
    return undefined;
}
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
        state.url = StaticPage.baseURL + nextProps.url;
        state.lang = `/?lang=${nextProps.intl.locale.split(/[_-]+/)[0]}`;
        this.htmlRequest(state.url + state.lang);
        this.setState(state);
        window.scrollTo(0, 0);
    }

    render() {
        const options = { transform };
        const { error, isLoaded, html } = this.state;
        const parsedHTML = ReactHTMLParser(html, options)[0];
        if (this.state.error) {
            return <div>Error: {error.message}</div>;
        } else if (!isLoaded) {
            return <div>Loading...</div>;
        }
        return (
            <div className={'container ldod-default'}>
                {parsedHTML.props.children}
            </div>
        );
    }
}


export default injectIntl(StaticPage);
