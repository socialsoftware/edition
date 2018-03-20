import React from 'react';
import ReactHTMLParser from 'react-html-parser';


export default class About extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            error: null,
            isLoaded: false,
            html: '',
        };
    }

    componentDidMount() {
        fetch(`http://1.1.1.10:8080/about/${this.props.url}`)
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
